package com.eirsteir.coffeewithme.commons.exception

import java.text.MessageFormat
import java.util.*


class APIException(propertiesConfig: PropertiesConfig) {

    class EntityNotFoundException(message: String?) : RuntimeException(message)
    class DuplicateEntityException(message: String?) : RuntimeException(message)
    class InvalidStatusChangeException(message: String?) : RuntimeException(message)


    companion object {
        private lateinit var propertiesConfig: PropertiesConfig

        fun of(
            entityType: EntityType, exceptionType: ExceptionType, vararg args: String
        ): RuntimeException {
            val messageTemplate: String = getMessageTemplate(entityType, exceptionType)
            return of(exceptionType, messageTemplate, *args)
        }

        private fun of(
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

    init {
        Companion.propertiesConfig = propertiesConfig
    }

}