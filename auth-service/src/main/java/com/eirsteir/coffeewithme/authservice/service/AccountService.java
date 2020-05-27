package com.eirsteir.coffeewithme.authservice.service;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.web.request.RegistrationRequest;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

    Account registerAccount(RegistrationRequest registrationRequest);

}
