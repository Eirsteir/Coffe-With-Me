package com.eirsteir.coffeewithme.commons.domain.notification;

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import io.eventuate.tram.events.common.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractEntityNotificationEvent implements DomainEvent {

  private Long subjectId;
  private UserDetails user;
}
