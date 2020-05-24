package com.eirsteir.coffeewithme.api.dto;

import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
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

    private Long requesterId;
    private Long addresseeId;
    private FriendshipStatus status;

}
