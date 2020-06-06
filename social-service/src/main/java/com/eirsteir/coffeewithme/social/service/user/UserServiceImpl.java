package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.exception.EntityType;
import com.eirsteir.coffeewithme.commons.exception.ExceptionType;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.university.University;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.dto.UserProfile;
import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.web.request.UpdateProfileRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UniversityRepository universityRepository;

    @Autowired
    private FriendshipService friendshipService;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetailsDto findUserByEmail(String email) {
        User userModel = userRepository.findByEmail(email)
                        .orElseThrow(() -> CWMException.getException(
                                EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email));

        return modelMapper.map(userModel, UserDetailsDto.class);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                        .orElseThrow(() -> CWMException.getException(EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, id.toString()));
    }

    @Override
    public UserDetailsDto findUserByIdWithIsFriend(Long id, Long currentUserId) {
        User user = findUserById(id);
        User currentUser = findUserById(id);
        UserDetailsDto userDetails = modelMapper.map(user, UserDetailsDto.class);
        this.addFriendshipPropertiesTo(userDetails, user, currentUser);

        return userDetails;
    }

    private void addFriendshipPropertiesTo(UserDetailsDto userDetails, User user, User currentUser) {
        List<User> friends = friendshipService.findFriends(user.getId(), FriendshipStatus.ACCEPTED);

        if (friends.contains(currentUser))
            userDetails.setIsFriend(true);
        else
            userDetails.setIsFriend(false);

        userDetails.setFriendsCount(friends.size());
    }

    @Override
    public List<UserDetailsDto> searchUsers(Specification<User> spec) {
        return userRepository.findAll(spec)
                .stream()
                .map(user -> modelMapper.map(user, UserDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserProfile updateProfile(UpdateProfileRequest updateProfileRequest, UserDetailsImpl currentUser) {
        User userToUpdate = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> CWMException.getException(EntityType.USER,
                                                             ExceptionType.ENTITY_NOT_FOUND,
                                                             currentUser.getId().toString()));

        Optional<University> university = universityRepository.findById(updateProfileRequest.getUniversityId());
        university.ifPresent(userToUpdate::setUniversity);

        userToUpdate.setNickname(updateProfileRequest.getNickname());
        log.info("[x] Updated user profile: {}", userToUpdate);

        return modelMapper.map(userRepository.save(userToUpdate), UserProfile.class);
    }

    @Override
    public UserDetails getUserDetails(User user) {
        return UserDetails.builder()
                .id(user.getId())
                .name(user.getName())
                .nickname(user.getNickname())
                .build();
    }

}
