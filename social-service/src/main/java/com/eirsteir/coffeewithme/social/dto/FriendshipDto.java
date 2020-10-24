package com.eirsteir.coffeewithme.social.dto;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto {

    private UserDetailsDto requester;
    private UserDetailsDto addressee;
    private FriendshipStatus status;

}
