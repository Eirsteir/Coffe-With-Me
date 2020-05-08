package com.eirsteir.coffeewithme.dto;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {

    private Long id;
    private String email;
    private String username;
    private String name;
    private String mobileNumber;

}
