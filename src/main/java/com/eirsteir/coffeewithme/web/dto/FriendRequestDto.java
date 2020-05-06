package com.eirsteir.coffeewithme.web.dto;


import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@Builder
@AllArgsConstructor
public class FriendRequestDto {

    @NotNull(message = "Must be a valid id")
    private Long id;

    @Email(message = "Must be a valid email")
    private String from;

    @Email(message = "Must be a valid email")
    private String to;

    @NotNull(message = "Must be a valid status")
    private FriendRequestStatus status;
    
}
