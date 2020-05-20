package com.eirsteir.coffeewithme.exception;

import com.eirsteir.coffeewithme.domain.MessageTemplateType;

public enum ExceptionType implements MessageTemplateType {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    ENTITY_EXCEPTION("exception"),
    INVALID_STATUS_CHANGE("invalid.state.change"),
    INVALID_STATE("invalid.state");

    String value;

    ExceptionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
