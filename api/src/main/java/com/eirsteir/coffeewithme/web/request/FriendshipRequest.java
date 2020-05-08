package com.eirsteir.coffeewithme.web.request;

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
public class FriendshipRequest implements Request {

    @NotNull(message = "Must be a valid id")
    private Long requesterId;

    @NotNull(message = "Must be a valid id")
    private Long addresseeId;

}
