package com.eirsteir.coffeewithme.commons.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private Long id;
    private String name;
    private String nickname;

}