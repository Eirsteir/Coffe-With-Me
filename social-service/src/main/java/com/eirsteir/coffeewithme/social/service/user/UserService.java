package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.commons.domain.UserDetails;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface UserService {

    UserDetailsDto findUserByEmail(String email);

    UserDetailsDto updateProfile(UpdateProfileRequest updateProfileRequest, UserDetailsImpl currentUser);

    User findUserById(Long id);

    List<UserDetailsDto> searchUsers(Specification<User> spec);

    UserDetailsDto findUserByIdWithIsFriend(Long id, Long viewerId);

    UserDetails getUserDetails(User user);

}
