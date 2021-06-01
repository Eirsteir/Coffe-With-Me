package com.eirsteir.coffeewithme.authservice.web.request

import com.eirsteir.coffeewithme.authservice.validation.ValidPassword
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank


data class RegistrationRequest(
    @get:Email(message = "Must be a well formed email")
    @get:NotBlank(message = "Email is required")
    val email: String,
    @field:ValidPassword
    val password: String,
    @get:NotBlank(message = "Name is required")
    val name:  String
)