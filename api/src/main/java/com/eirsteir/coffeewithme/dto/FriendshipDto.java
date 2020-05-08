package com.eirsteir.coffeewithme.dto;

import com.eirsteir.coffeewithme.domain.FriendshipId;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto {

    private FriendshipId id;
    private FriendshipStatus status;

}
