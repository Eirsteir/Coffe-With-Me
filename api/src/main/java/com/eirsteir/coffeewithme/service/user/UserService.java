package com.eirsteir.coffeewithme.service.user;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserRegistrationRequest userRegistrationRequest);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    User findUserById(Long id);

    List<UserDto> searchUsers(Specification<User> spec);

    UserDto findUserById(Long id, User currentUser);
}
