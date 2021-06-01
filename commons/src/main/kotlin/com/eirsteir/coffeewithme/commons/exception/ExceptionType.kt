package com.eirsteir.coffeewithme.commons.exception

enum class ExceptionType(var value: String) {
    ENTITY_NOT_FOUND("not.found"),
    DUPLICATE_ENTITY("duplicate"),
    INVALID_STATUS_CHANGE("invalid.status.change");
}