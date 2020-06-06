package com.eirsteir.coffeewithme.social.dto;

import com.eirsteir.coffeewithme.social.domain.university.University;
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
    private University university;

}
