package com.eirsteir.coffeewithme.domain.request;

import com.eirsteir.coffeewithme.domain.user.UserRequestRelation;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Getter
@Setter
@ToString
@SuperBuilder
@NoArgsConstructor
@Entity
public class Friendship extends UserRequestRelation {

    @Id
    @GeneratedValue
    private Long id;

}
