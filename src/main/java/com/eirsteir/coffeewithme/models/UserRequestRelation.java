package com.eirsteir.coffeewithme.models;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@MappedSuperclass
abstract class UserRequestRelation extends CreatedUpdatedDateBaseModel {

    @OneToOne
    @NotNull(message = "From (user) may not be null")
    private User from;

    @OneToOne
    @NotNull(message = "To (user) may not be null")
    private User to;

}
