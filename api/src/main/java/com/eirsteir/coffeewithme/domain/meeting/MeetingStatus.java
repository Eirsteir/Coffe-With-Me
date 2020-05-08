package com.eirsteir.coffeewithme.domain.meeting;

import com.eirsteir.coffeewithme.domain.RequestStatus;

public enum MeetingStatus implements RequestStatus {

    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    CHANGE_OF_TIME_REQUESTED(4);

    private Integer value;

    MeetingStatus(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return this.name();
    }

    public Integer getValue() {
        return value;
    }

}
