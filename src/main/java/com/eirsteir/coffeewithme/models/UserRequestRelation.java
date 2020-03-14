package com.eirsteir.coffeewithme.models;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
abstract class UserRequestRelation extends CreatedUpdatedDateBaseModel {

    @OneToOne
    private User from;

    @OneToOne
    private User to;

}
