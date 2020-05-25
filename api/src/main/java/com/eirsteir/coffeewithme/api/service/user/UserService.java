package com.eirsteir.coffeewithme.api.service.user;

import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    UserDetails registerUser(UserDetails userDetails);

    UserDetails findUserByEmail(String email);

    UserDetails updateProfile(UserDetails userDetails);

    User findUserById(Long id);

    List<UserDetails> searchUsers(Specification<User> spec);

    UserDetails findUserByIdWithIsFriend(Long id, Long viewerId);

}
