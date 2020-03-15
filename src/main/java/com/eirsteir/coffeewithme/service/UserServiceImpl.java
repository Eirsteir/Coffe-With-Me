package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.models.User;
import com.eirsteir.coffeewithme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUserByUsername(String username) {
        return null;
//        Optional<User> foundUser = userRepository.findByUsername(username);
//                .ifPresent();
    }
}
