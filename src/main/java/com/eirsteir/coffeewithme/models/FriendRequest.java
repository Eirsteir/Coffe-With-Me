package com.eirsteir.coffeewithme.models;

import com.eirsteir.coffeewithme.enums.FriendRequestStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class FriendRequest extends Request {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private FriendRequestStatus status;

    public FriendRequest() {
    }
}
