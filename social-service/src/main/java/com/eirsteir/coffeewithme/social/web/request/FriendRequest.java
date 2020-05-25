package com.eirsteir.coffeewithme.social.web.request;

import lombok.*;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;


@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequest {

    @NotNull(message = "Must be a valid id")
    private Long requesterId;

    @NotNull(message = "Must be a valid id")
    private Long addresseeId;

}
