package com.eirsteir.coffeewithme.authservice.service;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.web.request.UserRegistrationRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService {

    Optional<Account> registerUser(UserRegistrationRequest registrationRequest);

    void dispatch(Account account);
}
