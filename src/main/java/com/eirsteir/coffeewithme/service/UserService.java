package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;

public interface UserService {

    UserDto registerUser(UserRegistrationRequest userRegistrationRequest);

    UserDto findUserByEmail(String email);

    UserDto findUserById(Long id);

    UserDto updateProfile(UserDto userDto);

}
