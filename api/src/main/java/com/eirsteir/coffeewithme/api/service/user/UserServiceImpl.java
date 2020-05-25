package com.eirsteir.coffeewithme.api.service.user;

import com.eirsteir.coffeewithme.api.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.api.domain.user.User;
import com.eirsteir.coffeewithme.api.exception.CWMException;
import com.eirsteir.coffeewithme.api.exception.ExceptionType;
import com.eirsteir.coffeewithme.api.repository.UserRepository;
import com.eirsteir.coffeewithme.api.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.commons.dto.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.api.exception.EntityType.USER;
import static com.eirsteir.coffeewithme.api.exception.ExceptionType.ENTITY_NOT_FOUND;

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

    @Autowired
    private PasswordEncoder passwordEncoder;

//    @Override
//    public UserDto registerUser(UserRegistrationRequest userRegistrationRequest) {
//        Optional<User> user = userRepository.findByEmail(userRegistrationRequest.getEmail());
//
//        if (user.isPresent())
//            throw CWMException.getException(
//                    USER, ExceptionType.DUPLICATE_ENTITY, userRegistrationRequest.getEmail());
//
//        User userModel = modelMapper.map(userRegistrationRequest, User.class);
//        userModel.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
//
//        User signedUpUserModel = userRepository.save(userModel);
//        log.info("[x] Registered user: {}", signedUpUserModel);
//
//        return modelMapper.map(signedUpUserModel, UserDto.class);
//    }

    @Override
    public UserDetails registerUser(UserDetails userDetails) {
        Optional<User> user = userRepository.findByEmail(userDetails.getEmail());

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
    public UserDetails findUserByIdWithIsFriend(Long id, User currentUser) {
        User user = findUserById(id);
        UserDetails userDetails = modelMapper.map(user, UserDetails.class);
        
        if (areFriends(currentUser, user))
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
