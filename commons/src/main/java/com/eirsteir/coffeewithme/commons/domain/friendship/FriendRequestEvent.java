package com.eirsteir.coffeewithme.commons.domain.friendship;

import com.eirsteir.coffeewithme.commons.domain.notification.AbstractEntityNotificationEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestEvent extends AbstractEntityNotificationEvent implements FriendshipEvent {

  public FriendRequestEvent(Long subjectId, UserDetails user) {
    super(subjectId, user);
  }
}
