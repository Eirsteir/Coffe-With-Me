package com.eirsteir.coffeewithme.authservice.web.api;

import com.eirsteir.coffeewithme.authservice.service.AccountService;
import com.eirsteir.coffeewithme.authservice.web.request.RegistrationRequest;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

  @Autowired private AccountService accountService;

  @CrossOrigin(origins = "*") // lock down on deployment
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  void register(@RequestBody @Valid RegistrationRequest registrationRequest) {
    accountService.registerAccount(registrationRequest);
  }
}
