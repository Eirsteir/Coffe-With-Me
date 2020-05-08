package com.eirsteir.coffeewithme.dto;


import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
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
    private FriendshipStatus status;

}
