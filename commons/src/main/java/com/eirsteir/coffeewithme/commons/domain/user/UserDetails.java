package com.eirsteir.coffeewithme.commons.domain.user;

import lombok.*;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDetails {

    private Long id;
    private String name;
    private String nickname;

}