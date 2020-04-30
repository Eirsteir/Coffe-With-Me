package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.user.NewUserForm;
import com.eirsteir.coffeewithme.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<User> getUserByUsername(String username);

    User saveUser(NewUserForm newUserForm);

    Optional<User> findById(Long id);

    List<User> getAllUsers();

    User update(User user);

}
