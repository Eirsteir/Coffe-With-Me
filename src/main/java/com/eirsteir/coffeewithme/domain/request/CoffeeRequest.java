package com.eirsteir.coffeewithme.domain.request;


import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import com.eirsteir.coffeewithme.domain.request.enums.CoffeeRequestStatus;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class CoffeeRequest extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private CoffeeRequestStatus status;

}
