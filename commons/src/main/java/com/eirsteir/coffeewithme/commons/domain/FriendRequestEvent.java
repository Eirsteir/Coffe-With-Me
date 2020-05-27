package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestEvent extends AbstractFriendshipEvent {

    public FriendRequestEvent(Long subjectId, UserDetails user) {
        super(subjectId, user);
    }

}
