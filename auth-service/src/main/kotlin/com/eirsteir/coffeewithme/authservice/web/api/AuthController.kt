package com.eirsteir.coffeewithme.authservice.web.api

import com.eirsteir.coffeewithme.authservice.service.AccountService
import com.eirsteir.coffeewithme.authservice.web.request.RegistrationRequest
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController(val accountService: AccountService) {

    @CrossOrigin(origins = ["*"]) // lock down on deployment
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody registrationRequest: @Valid RegistrationRequest) =
        accountService.registerAccount(registrationRequest)

}