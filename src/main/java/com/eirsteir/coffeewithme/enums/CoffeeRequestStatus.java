package com.eirsteir.coffeewithme.enums;

import com.eirsteir.coffeewithme.domain.RequestStatus;

public enum CoffeeRequestStatus implements RequestStatus {

    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    CHANGE_OF_TIME_REQUESTED(4);

    private Integer value;

    CoffeeRequestStatus(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return this.name();
    }

    public Integer getValue() {
        return value;
    }

}
