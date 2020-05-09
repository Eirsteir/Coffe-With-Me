package com.eirsteir.coffeewithme.dto;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.web.request.IdentifiableFriendship;
import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto implements IdentifiableFriendship {

    private FriendshipIdDto id;
    private FriendshipStatus status;

    @Override
    public Long getRequester() {
        return id.getRequester().getId();
    }
}
