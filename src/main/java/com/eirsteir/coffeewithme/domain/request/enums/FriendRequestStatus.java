package com.eirsteir.coffeewithme.domain.request.enums;


import com.eirsteir.coffeewithme.domain.request.RequestStatus;

public enum FriendRequestStatus implements RequestStatus {

    PENDING(1),
    ACCEPTED(2),
    REJECTED(3),
    CANCELLED(4);

    private Integer value;

    FriendRequestStatus(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return this.name();
    }

    public Integer getValue() {
        return value;
    }



}
