package com.eirsteir.coffeewithme.domain.user;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@MappedSuperclass
public abstract class UserRequestRelation extends CreatedUpdatedDateBaseModel {

    @OneToOne
    @NotNull(message = "From (user) may not be null")
    private User from;

    @OneToOne
    @NotNull(message = "To (user) may not be null")
    private User to;

}
