package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.dto.UserDto;

import java.util.List;

public interface UserService {

    String signUp(UserDto user);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    List<UserDto> findAll();
}
