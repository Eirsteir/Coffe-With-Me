package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.NewUserForm;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.UserDto;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;


    @Override
    public UserDto signUp(UserDto userDto) {
        return null;
    }

    @Override
    public UserDto findUserByEmail(String email) {
        return null;
    }

    @Override
    public UserDto updateProfile(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isPresent()) {
            User userModel = user.get();
            userModel.setUsername(userDto.getUsername())
                    .setName(userDto.getName())
                    .setMobileNumber(userDto.getMobileNumber());
            return UserMapper.toUserDto(userRepository.save(userModel));
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }

    @Override
    public UserDto changePassword(UserDto userDto, String newPassword) {
        return null;
    }

}
