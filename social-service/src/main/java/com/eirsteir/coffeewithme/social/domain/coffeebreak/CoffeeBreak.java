package com.eirsteir.coffeewithme.social.domain.coffeebreak;


import com.eirsteir.coffeewithme.social.domain.CreatedUpdatedDateTimeBaseModel;
import com.eirsteir.coffeewithme.social.domain.university.Campus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
public class CoffeeBreak extends CreatedUpdatedDateTimeBaseModel {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private LocalTime scheduledTo;

  @ManyToOne
  private User requester;

  @ManyToMany
  private Set<User> addressees;

  @ManyToOne
  private Campus campus;

}
