package com.eirsteir.coffeewithme.commons.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {

    private Long id;
    private String email;
    private String username;
    private String name;
    private String mobileNumber;
    private Boolean isFriend;

}
