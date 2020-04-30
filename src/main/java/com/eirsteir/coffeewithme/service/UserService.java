package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.dto.UserDto;

public interface UserService {

    UserDto signUp(UserDto userDto);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    UserDto changePassword(UserDto userDto, String newPassword);
}
