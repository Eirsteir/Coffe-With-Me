package com.eirsteir.coffeewithme.social.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@ToString
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    private Long id;
    private String email;
    private String nickname;
    private String name;
    private Integer friendsCount;

}
