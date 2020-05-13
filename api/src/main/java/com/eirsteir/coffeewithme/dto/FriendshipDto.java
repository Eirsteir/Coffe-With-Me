package com.eirsteir.coffeewithme.dto;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipPk;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
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

    private FriendshipPk id; // TODO: 13.05.2020 dont use this model here
    private FriendshipStatus status;

}
