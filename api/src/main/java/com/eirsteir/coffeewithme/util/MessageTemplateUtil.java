package com.eirsteir.coffeewithme.util;

import com.eirsteir.coffeewithme.config.PropertiesConfig;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;


@Component
public class MessageTemplateUtil {

    public static PropertiesConfig propertiesConfig;

    @Autowired
    public MessageTemplateUtil(PropertiesConfig propertiesConfig) {
        MessageTemplateUtil.propertiesConfig = propertiesConfig;
    }

    public static String getMessageTemplate(Enum<?> type) {
        return type
                .name()
                .concat(".")
                .toLowerCase();
    }

    public static String getExceptionMessageTemplate(EntityType entityType, ExceptionType exceptionType) {
        return getMessageTemplate(entityType)
                .concat(exceptionType.getValue())
                .toLowerCase();
    }

    public static String format(String template, String... args) {
        Optional<String> templateContent = Optional.ofNullable(propertiesConfig.getConfigValue(template));
        return templateContent.map(s -> MessageFormat.format(s, args))
                .orElseGet(() -> String.format(template, args));
    }
}