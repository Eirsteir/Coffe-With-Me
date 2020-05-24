package com.eirsteir.coffeewithme.api.exception;

import org.springframework.stereotype.Component;

@Component
public class CWMException {

    public static RuntimeException getException(EntityType entityType, ExceptionType exceptionType, String... args) {
        String messageTemplate = MessageTemplateUtil.getMessageTemplate(entityType, exceptionType);
        return getException(exceptionType, messageTemplate, args);
    }

    public static class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }

    public static class DuplicateEntityException extends RuntimeException {
        public DuplicateEntityException(String message) {
            super(message);
        }
    }

    public static class InvalidStatusChangeException extends RuntimeException {
        public InvalidStatusChangeException(String message) {
            super(message);
        }
    }

    private static RuntimeException getException(ExceptionType exceptionType, String messageTemplate, String... args) {
        if (ExceptionType.ENTITY_NOT_FOUND.equals(exceptionType))
            return new EntityNotFoundException(MessageTemplateUtil.format(messageTemplate, args));
        else if (ExceptionType.DUPLICATE_ENTITY.equals(exceptionType))
            return new DuplicateEntityException(MessageTemplateUtil.format(messageTemplate, args));
        else if (ExceptionType.INVALID_STATUS_CHANGE.equals(exceptionType))
            return new InvalidStatusChangeException(MessageTemplateUtil.format(messageTemplate, args));

        return new RuntimeException(MessageTemplateUtil.format(messageTemplate, args));
    }

}