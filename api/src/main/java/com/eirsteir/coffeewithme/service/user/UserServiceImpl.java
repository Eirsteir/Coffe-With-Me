package com.eirsteir.coffeewithme.service.user;

import com.eirsteir.coffeewithme.domain.friendship.Friendship;
import com.eirsteir.coffeewithme.domain.friendship.FriendshipStatus;
import com.eirsteir.coffeewithme.domain.role.RoleType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.domain.user.UserType;
import com.eirsteir.coffeewithme.dto.FriendshipDto;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.repository.RoleRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.web.request.FriendRequest;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.exception.EntityType.USER;
import static com.eirsteir.coffeewithme.exception.ExceptionType.ENTITY_NOT_FOUND;

@Slf4j
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto registerUser(UserRegistrationRequest userRegistrationRequest) {
        Optional<User> user = userRepository.findByEmail(userRegistrationRequest.getEmail());

        if (user.isPresent())
            throw CWMException.getException(
                    USER, ExceptionType.DUPLICATE_ENTITY, userRegistrationRequest.getEmail());

        User userModel = modelMapper.map(userRegistrationRequest, User.class);
        userModel.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()))
            .setUserType(UserType.LOCAL)
            .setRoles(Collections.singletonList(
                    roleRepository.findByType(RoleType.ROLE_USER)));

        User signedUpUserModel = userRepository.save(userModel);
        log.info("[x] Registered user: {}", signedUpUserModel);

        return modelMapper.map(signedUpUserModel, UserDto.class);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User userModel = userRepository.findByEmail(email)
                        .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, email));

        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                        .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, id.toString()));
    }

    @Override
    public List<UserDto> getFriends(User user) {

        return user.getFriends()
                .stream()
                .map(Friendship::getAddressee)
                .map(friend -> modelMapper.map(friend, UserDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FriendshipDto registerFriendship(FriendRequest friendRequest) {
        User requester = modelMapper.map(findUserById(friendRequest.getRequesterId()), User.class);
        User addressee = modelMapper.map(findUserById(friendRequest.getAddresseeId()), User.class);

        if (friendshipExists(requester, addressee))
            throw CWMException.getException(EntityType.FRIENDSHIP,
                                            ExceptionType.DUPLICATE_ENTITY,
                                            requester.getId().toString(),
                                            addressee.getId().toString());

        Friendship friendship = requester.addFriend(addressee, FriendshipStatus.REQUESTED);
        return modelMapper.map(friendship, FriendshipDto.class);
    }

    private boolean friendshipExists(User requester, User addressee) {
        return friendshipRepository.existsByPkRequesterAndPkAddressee(requester, addressee);
    }

    @Override
    public UserDto updateProfile(UserDto userDto) {
        User userModel = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, userDto.getEmail()));

        userModel.setUsername(userDto.getUsername());
        log.info("[x] Updated user profile: {}", userModel);

        return modelMapper.map(userRepository.save(userModel), UserDto.class);
    }

}
