package com.eirsteir.coffeewithme.social.domain.meeting;

import com.eirsteir.coffeewithme.social.domain.RequestStatus;

public enum MeetingStatus implements RequestStatus {

    REQUESTED(1),
    ACCEPTED(2),
    DECLINED(3),
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
