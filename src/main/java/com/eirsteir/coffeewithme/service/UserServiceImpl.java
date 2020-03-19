package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
