package com.eirsteir.coffeewithme.commons.domain.coffeebreak;

import com.eirsteir.coffeewithme.commons.domain.university.CampusDetails;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoffeeBreakDetails {

  private LocalTime scheduledTo;
  private UserDetails requester;
  private Set<UserDetails> addressees;
  private CampusDetails campus;
}
