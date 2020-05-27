package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestEvent extends AbstractFriendshipEvent {

    public FriendRequestEvent(UserDetails user) {
        super(user);
    }

}
