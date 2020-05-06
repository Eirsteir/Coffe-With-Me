package com.eirsteir.coffeewithme.exception;

public enum ExceptionType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    INVALID_STATE_CHANGE("invalid.state.change"),
    INVALID_STATE("invalid.state");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    String getValue() {
        return this.value;
    }
}
