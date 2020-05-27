package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestAcceptedEvent extends AbstractFriendshipEvent {

    public FriendRequestAcceptedEvent(Long subjectId, UserDetails user) {
        super(subjectId, user);
    }

}
