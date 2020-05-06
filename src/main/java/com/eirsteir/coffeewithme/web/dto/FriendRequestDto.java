package com.eirsteir.coffeewithme.web.dto;


import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {

    @NotNull(message = "Must be a valid id")
    private Long id;

    @NotNull(message = "Must be a valid id")
    private Long from;

    @NotNull(message = "Must be a valid id")
    private Long to;

    @NotNull(message = "Must be a valid status")
    private FriendRequestStatus status;

}
