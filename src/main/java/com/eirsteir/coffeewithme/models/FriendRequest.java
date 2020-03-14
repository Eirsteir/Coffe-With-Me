package com.eirsteir.coffeewithme.models;


import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class FriendRequest extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;
}
