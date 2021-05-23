package com.eirsteir.coffeewithme.social.web.request

import javax.validation.constraints.Max
import javax.validation.constraints.Positive


data class CoffeeBreakRequest(
    @get:Max(
        value = 60 * 2,
        message = "Coffee break cannot be scheduled to start in more than 2 hours"
    )
    @get:Positive(message = "Must be a positive number")
    val scheduledToInMinutes: Long,
    val campusId: Long
)