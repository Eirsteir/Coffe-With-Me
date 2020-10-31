package com.eirsteir.coffeewithme.social.util;

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import com.eirsteir.coffeewithme.social.domain.user.User;

public class UserServiceUtils {

  public static UserDetails getUserDetailsFrom(User user) {
    return UserDetails.builder()
        .id(user.getId())
        .name(user.getName())
        .nickname(user.getNickname())
        .build();
  }
}
