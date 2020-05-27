package com.eirsteir.coffeewithme.commons.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractFriendshipEvent  implements FriendshipEvent {

    private Long subjectId;
    private UserDetails user;

}
