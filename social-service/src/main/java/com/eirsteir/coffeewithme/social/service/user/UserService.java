package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public interface UserService {

  UserDetailsDto findUserByEmail(String email);

  UserProfile updateProfile(UpdateProfileRequest updateProfileRequest, UserDetailsImpl currentUser);

  User findUserById(Long id);

  List<UserDetailsDto> searchUsers(Specification<User> spec);

  UserDetailsDto findUserById(Long id, Long viewerId);

  List<User> findByIdIn(List<Long> friendsIds);
}
