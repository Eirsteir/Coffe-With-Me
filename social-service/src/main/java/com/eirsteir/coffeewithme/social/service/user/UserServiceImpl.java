package com.eirsteir.coffeewithme.social.service.user;

import com.eirsteir.coffeewithme.social.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.exception.CWMException;
import com.eirsteir.coffeewithme.social.exception.ExceptionType;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.commons.dto.UserDetailsDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.social.exception.EntityType.USER;
import static com.eirsteir.coffeewithme.social.exception.ExceptionType.ENTITY_NOT_FOUND;

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
    public UserDetailsDto registerUser(UserDetailsDto UserDetailsDto) {
        Optional<User> user = userRepository.findByEmail(UserDetailsDto.getEmail());

        if (user.isPresent())
            throw CWMException.getException(
                    USER, ExceptionType.DUPLICATE_ENTITY, userDetails.getEmail());

        User userModel = modelMapper.map(userDetails, User.class);

        User signedUpUserModel = userRepository.save(userModel);
        log.info("[x] Registered user: {}", signedUpUserModel);

        return modelMapper.map(signedUpUserModel, UserDetails.class);
    }

    @Override
    public UserDetails findUserByEmail(String email) {
        User userModel = userRepository.findByEmail(email)
                        .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, email));

        return modelMapper.map(userModel, UserDetails.class);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                        .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, id.toString()));
    }

    @Override
    public UserDetails findUserByIdWithIsFriend(Long id, Long viewerId) {
        User user = findUserById(id);
        User viewer = findUserById(id);
        UserDetails userDetails = modelMapper.map(user, UserDetails.class);
        
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
    public List<UserDetails> searchUsers(Specification<User> spec) {
        return userRepository.findAll(spec)
                .stream()
                .map(user -> modelMapper.map(user, UserDetails.class))
                .collect(Collectors.toList());
    }

    @Override
    public UserDetails updateProfile(UserDetails userDetails) {
        User userModel = userRepository.findByEmail(userDetails.getEmail())
                .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, userDetails.getEmail()));

        userModel.setUsername(userDetails.getUsername());
        log.info("[x] Updated user profile: {}", userModel);

        return modelMapper.map(userRepository.save(userModel), UserDetails.class);
    }

}
