package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.commons.domain.UserDetails;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import com.eirsteir.coffeewithme.commons.exception.CWMException;
import com.eirsteir.coffeewithme.commons.exception.EntityType;
import com.eirsteir.coffeewithme.commons.exception.ExceptionType;
import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
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
    private FriendshipService friendshipService;
    
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public UserDetailsDto registerUser(UserDetailsDto userDetailsDto) {
        Optional<User> user = userRepository.findByEmail(userDetailsDto.getEmail());

        if (user.isPresent())
            throw CWMException.getException(
                    EntityType.USER, ExceptionType.DUPLICATE_ENTITY, userDetailsDto.getEmail());

        User userModel = modelMapper.map(userDetailsDto, User.class);

        User signedUpUserModel = userRepository.save(userModel);
        log.info("[x] Registered user: {}", signedUpUserModel);

        return modelMapper.map(signedUpUserModel, UserDetailsDto.class);
    }

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
    public UserDetailsDto findUserByIdWithIsFriend(Long id, Long viewerId) {
        User user = findUserById(id);
        User viewer = findUserById(id);
        UserDetailsDto userDetails = modelMapper.map(user, UserDetailsDto.class);
        
        if (areFriends(viewer, user))
            userDetails.setIsFriend(true);
        else
            userDetails.setIsFriend(false);

        return userDetails;
    }

    private boolean areFriends(User currentUser, User user) {
        return friendshipService.findFriends(currentUser.getId(), FriendshipStatus.ACCEPTED).contains(user);
    }

    @Override
    public List<UserDetailsDto> searchUsers(Specification<User> spec) {
        return userRepository.findAll(spec)
                .stream()
                .map(user -> modelMapper.map(user, UserDetailsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailsDto updateProfile(UserDetailsDto UserDetailsDto) {
        User userModel = userRepository.findByEmail(UserDetailsDto.getEmail())
                .orElseThrow(() -> CWMException.getException(
                        EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, UserDetailsDto.getEmail()));

        userModel.setNickname(UserDetailsDto.getUsername());
        log.info("[x] Updated user profile: {}", userModel);

        return modelMapper.map(userRepository.save(userModel), UserDetailsDto.class);
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
