package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.domain.User;

import java.util.Optional;

public interface UserService {

    Optional<User> getUserByUsername(String username);

}
