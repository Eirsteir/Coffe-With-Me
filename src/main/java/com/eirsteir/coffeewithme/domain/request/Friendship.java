package com.eirsteir.coffeewithme.domain.request;

import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Friendship extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

}
