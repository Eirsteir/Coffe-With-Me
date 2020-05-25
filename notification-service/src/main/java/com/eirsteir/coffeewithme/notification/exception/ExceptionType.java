package com.eirsteir.coffeewithme.notification.exception;

public enum ExceptionType {

    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
