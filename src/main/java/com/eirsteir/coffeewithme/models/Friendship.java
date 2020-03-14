package com.eirsteir.coffeewithme.models;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Data
@Entity
public class Friendship extends CreatedUpdatedDateBaseModel {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User from;

    @OneToOne
    private User to;


}
