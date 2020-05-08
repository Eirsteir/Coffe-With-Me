package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;

import java.util.Collection;

public interface UserService {

    UserDto registerUser(UserRegistrationRequest userRegistrationRequest);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    UserDto findUserById(Long id);

}
