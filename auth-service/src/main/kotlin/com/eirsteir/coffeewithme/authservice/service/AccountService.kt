package com.eirsteir.coffeewithme.authservice.service

import com.eirsteir.coffeewithme.authservice.domain.Account
import com.eirsteir.coffeewithme.authservice.web.request.RegistrationRequest
import org.springframework.stereotype.Service

interface AccountService {
    fun registerAccount(registrationRequest: RegistrationRequest): Account
}