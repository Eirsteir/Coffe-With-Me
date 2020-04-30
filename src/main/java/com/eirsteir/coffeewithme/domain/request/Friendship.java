package com.eirsteir.coffeewithme.domain.request;

import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Friendship extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

}
