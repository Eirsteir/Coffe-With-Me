package com.eirsteir.coffeewithme.dto;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.web.request.Request;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto implements Request {

    private UserDto requester;
    private UserDto addressee;
    private FriendshipStatus status;

    @Override
    public Long getRequesterId() {
        return requester.getId();
    }
}
