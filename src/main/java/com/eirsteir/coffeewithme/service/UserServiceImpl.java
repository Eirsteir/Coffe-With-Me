package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.eirsteir.coffeewithme.exception.EntityType.USER;
import static com.eirsteir.coffeewithme.exception.ExceptionType.ENTITY_NOT_FOUND;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public UserDto signUp(UserDto userDto) {
        System.out.println(userDto);
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());

        if (user.isPresent())
            throw CWMException.throwException(
                    USER, ExceptionType.DUPLICATE_ENTITY, userDto.getEmail());

        User userModel = modelMapper.map(userDto, User.class);
        userModel.setPassword(bCryptPasswordEncoder.encode(userModel.getPassword()));
        User signedUpUser = userRepository.save(userModel);

        return modelMapper.map(signedUpUser, UserDto.class);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        User userModel = userRepository.findByEmail(email)
                        .orElseThrow(() -> CWMException.throwException(USER, ENTITY_NOT_FOUND, email));

        return modelMapper.map(userModel, UserDto.class);
    }

    @Override
    public UserDto updateProfile(UserDto userDto) {
        User userModel = userRepository.findByEmail(userDto.getEmail())
                .orElseThrow(() -> CWMException.throwException(USER, ENTITY_NOT_FOUND, userDto.getEmail()));

        userModel.setUsername(userDto.getUsername());

        return modelMapper.map(userRepository.save(userModel), UserDto.class);
    }

}
