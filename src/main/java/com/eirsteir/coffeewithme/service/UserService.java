package com.eirsteir.coffeewithme.service;

import com.eirsteir.coffeewithme.models.User;

public interface UserService {

    User getUserByUsername(String username);

}
