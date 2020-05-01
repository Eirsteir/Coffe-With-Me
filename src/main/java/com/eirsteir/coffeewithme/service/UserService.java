package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.dto.UserDto;

public interface UserService {

    UserDto loginOrSignUp(UserDto userDto);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);
}
