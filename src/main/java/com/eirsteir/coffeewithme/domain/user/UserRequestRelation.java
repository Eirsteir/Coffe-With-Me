package com.eirsteir.coffeewithme.domain.user;

import com.eirsteir.coffeewithme.domain.CreatedUpdatedDateBaseModel;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@Getter
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class UserRequestRelation extends CreatedUpdatedDateBaseModel {

    @OneToOne
    private User from;

    @OneToOne
    private User to;

    public UserRequestRelation() {
        super();
    }
}
