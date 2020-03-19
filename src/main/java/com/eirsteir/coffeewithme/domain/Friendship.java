package com.eirsteir.coffeewithme.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Friendship extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

}
