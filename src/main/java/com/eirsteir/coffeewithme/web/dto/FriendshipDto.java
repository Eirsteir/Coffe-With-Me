package com.eirsteir.coffeewithme.web.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FriendshipDto {

    private Long id;
    private Long from;
    private Long to;

}
