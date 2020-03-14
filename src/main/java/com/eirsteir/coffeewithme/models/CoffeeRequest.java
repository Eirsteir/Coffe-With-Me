package com.eirsteir.coffeewithme.models;


import com.eirsteir.coffeewithme.enums.CoffeeRequestStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class CoffeeRequest extends CreatedUpdatedDateBaseModel {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private CoffeeRequestStatus status;

}
