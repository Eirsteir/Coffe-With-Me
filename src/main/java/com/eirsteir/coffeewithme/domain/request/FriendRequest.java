package com.eirsteir.coffeewithme.domain.request;


import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class FriendRequest extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private FriendRequestStatus status;

}
