package com.eirsteir.coffeewithme.domain;


import com.eirsteir.coffeewithme.enums.FriendRequestStatus;
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
