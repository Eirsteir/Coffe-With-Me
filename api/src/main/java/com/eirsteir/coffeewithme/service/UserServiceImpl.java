package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.role.RoleType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.domain.user.UserType;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.RoleRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.web.dto.UserDto;
import com.eirsteir.coffeewithme.web.request.UserRegistrationRequest;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
    public UserDto findUserById(Long id) {
        User userModel = userRepository.findById(id)
                        .orElseThrow(() -> CWMException.getException(USER, ENTITY_NOT_FOUND, id.toString()));

        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public Collection<UserDto> findAllFriends(UserDto userDto) {
        return userRepository.findAllFriends(modelMapper.map(userDto, User.class));
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
