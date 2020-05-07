package com.eirsteir.coffeewithme.web.dto;


import com.eirsteir.coffeewithme.domain.request.enums.FriendRequestStatus;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendRequestDto {

    private Long id;
    private Long from;
    private Long to;
    private FriendRequestStatus status;

}
