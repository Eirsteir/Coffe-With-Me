package com.eirsteir.coffeewithme.domain;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@Getter
@ToString
@SuperBuilder
@MappedSuperclass
public abstract class CreatedUpdatedDateBaseModel {

    @CreationTimestamp
    private Date createdAt;

    @UpdateTimestamp
    private Date updatedAt;

    public CreatedUpdatedDateBaseModel() {
    }
}
