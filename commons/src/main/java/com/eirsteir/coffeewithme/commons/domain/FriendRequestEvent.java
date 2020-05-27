package com.eirsteir.coffeewithme.commons.domain;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class FriendRequestEvent extends AbstractEntityNotificationEvent  implements FriendshipEvent{

    public FriendRequestEvent(Long subjectId, UserDetails user) {
        super(subjectId, user);
    }

}
