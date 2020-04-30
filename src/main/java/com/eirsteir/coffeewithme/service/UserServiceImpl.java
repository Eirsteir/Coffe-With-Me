package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.NewUserForm;
import com.eirsteir.coffeewithme.domain.user.User;
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
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User update(User user) {
        return userRepository.save(user);
    }

    @Override
    public User saveUser(NewUserForm newUserForm) {
        return userRepository.save(User.builder()
                .username(newUserForm.getUsername())
                .email(newUserForm.getEmail())
                .password(newUserForm.getPassword())
                .build());
    }

}
