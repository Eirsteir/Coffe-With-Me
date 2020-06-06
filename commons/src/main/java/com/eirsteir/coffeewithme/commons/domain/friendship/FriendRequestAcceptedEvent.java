package com.eirsteir.coffeewithme.commons.domain.friendship;

import com.eirsteir.coffeewithme.commons.domain.notification.AbstractEntityNotificationEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestAcceptedEvent extends AbstractEntityNotificationEvent implements FriendshipEvent {

    public FriendRequestAcceptedEvent(Long subjectId, UserDetails user) {
        super(subjectId, user);
    }

}
