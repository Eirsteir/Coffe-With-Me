package com.eirsteir.coffeewithme.domain.request;


import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@SuperBuilder
@Entity
public class FriendRequest extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private FriendRequestStatus status;

    public FriendRequest() {
    }
}
