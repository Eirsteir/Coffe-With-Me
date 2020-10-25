package com.eirsteir.coffeewithme.commons.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Data
@Builder
@EqualsAndHashCode
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailsDto {

    private Long id;
    private String email;
    private String nickname;
    private String name;
    private Boolean isFriend;
    private Integer friendsCount;

}
