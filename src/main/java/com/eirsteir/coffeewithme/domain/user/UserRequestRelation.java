package com.eirsteir.coffeewithme.domain.user;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class UserRequestRelation extends CreatedUpdatedDateBaseModel {

    @OneToOne
    @NotNull(message = "From (user) may not be null")
    private User from;

    @OneToOne
    @NotNull(message = "To (user) may not be null")
    private User to;

    public UserRequestRelation() {
        super();
    }
}
