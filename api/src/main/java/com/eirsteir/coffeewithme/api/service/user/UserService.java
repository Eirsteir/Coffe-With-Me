package com.eirsteir.coffeewithme.api.service.user;

import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.dto.UserDto;
import com.eirsteir.coffeewithme.api.web.request.UserRegistrationRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    UserDto registerUser(UserRegistrationRequest userRegistrationRequest);

    UserDto findUserByEmail(String email);

    UserDto updateProfile(UserDto userDto);

    User findUserById(Long id);

    List<UserDto> searchUsers(Specification<User> spec);

    UserDto findUserByIdWithIsFriend(Long id, User currentUser);
}
