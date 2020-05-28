package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestAcceptedEvent extends AbstractEntityNotificationEvent implements FriendshipEvent {

    public FriendRequestAcceptedEvent(Long subjectId, UserDetails user) {
        super(subjectId, user);
    }

}
