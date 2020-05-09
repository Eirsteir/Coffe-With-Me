package com.eirsteir.coffeewithme.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipIdDto {

    private UserDto requester;
    private UserDto addressee;

}
