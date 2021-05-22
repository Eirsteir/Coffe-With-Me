package com.eirsteir.coffeewithme.commons.exception

import com.eirsteir.coffeewithme.commons.exception.MessageTemplateUtil
import org.springframework.stereotype.Component
import java.text.MessageFormat
import java.util.*


class CWMException(private val propertiesConfig: PropertiesConfig) {

    class EntityNotFoundException(message: String?) : RuntimeException(message)
    class DuplicateEntityException(message: String?) : RuntimeException(message)
    class InvalidStatusChangeException(message: String?) : RuntimeException(message)

    fun getException(
        entityType: EntityType, exceptionType: ExceptionType, vararg args: String
    ): RuntimeException {
        val messageTemplate: String = getMessageTemplate(entityType, exceptionType)
        return getException(exceptionType, messageTemplate, *args)
    }

    private fun getException(
        exceptionType: ExceptionType, messageTemplate: String, vararg args: String
    ) =
        when {
            ExceptionType.ENTITY_NOT_FOUND == exceptionType -> EntityNotFoundException(
                format(
                    messageTemplate,
                    *args
                )
            )
            ExceptionType.DUPLICATE_ENTITY == exceptionType -> DuplicateEntityException(
                format(
                    messageTemplate,
                    *args
                )
            )
            ExceptionType.INVALID_STATUS_CHANGE == exceptionType -> InvalidStatusChangeException(
                format(messageTemplate, *args)
            )
            else -> RuntimeException(format(messageTemplate, *args))
        }

    private fun getMessageTemplate(entityType: EntityType, exceptionType: ExceptionType): String {
        return entityType.name + "." + exceptionType.value.lowercase()
    }

    private fun format(template: String, vararg args: String): String {
        val templateContent = Optional.ofNullable(
            propertiesConfig.getConfigValue(template)
        )
        return templateContent
            .map { s: String -> MessageFormat.format(s, *args) }
            .orElseGet { String.format(template, *args) }
    }
}