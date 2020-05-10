package com.eirsteir.coffeewithme.domain.friendship;


import com.eirsteir.coffeewithme.domain.RequestStatus;

public enum FriendshipStatus implements RequestStatus {

    REQUESTED(1),
    ACCEPTED(2),
    DECLINED(3),
    BLOCKED(4);

    private Integer value;

    FriendshipStatus(Integer value) {
        this.value = value;
    }

    public String getStatus() {
        return this.name();
    }

    public Integer getValue() {
        return value;
    }

}
