package com.eirsteir.coffeewithme.social.domain.coffeebreak;


import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.social.domain.university.Campus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@Entity
public class CoffeeBreak extends CreatedUpdatedDateTimeBaseModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  private User requester;

  @ManyToMany
  private Set<User> addressees;

  @ManyToOne
  private Campus campus;
  
}
