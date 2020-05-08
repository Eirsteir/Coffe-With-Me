package com.eirsteir.coffeewithme.web.request;

import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@Accessors(chain = true)
@NoArgsConstructor
public class FriendRequestInquiry {

    @NotNull(message = "Must be a valid id")
    private Long id;

    @NotNull(message = "Must be a valid id")
    private Long from;

    @NotNull(message = "Must be a valid id")
    private Long to;

    @NotNull(message = "Must be a valid status")
    private FriendshipStatus status;

}
