package com.eirsteir.coffeewithme.domain.notification;

import com.eirsteir.coffeewithme.domain.MessageTemplateType;

public enum NotificationType implements MessageTemplateType  {

    REQUESTED("requested"),
    ACCEPTED("accepted");

    String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

}
