package com.eirsteir.coffeewithme.web.dto;

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
    private String status;

    @Override
    public Long getRequesterId() {
        return requester.getId();
    }
}
