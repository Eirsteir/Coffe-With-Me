package com.eirsteir.coffeewithme.social.web.request

import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class UpdateProfileRequest(
    @get:NotBlank(message = "Field is required")
    val nickname: String,
    @get:NotNull(message = "Field is required")
    @get:Positive(message = "Must be a positive integer")
    val universityId: Long
)
