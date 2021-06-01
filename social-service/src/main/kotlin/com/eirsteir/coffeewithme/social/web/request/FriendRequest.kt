package com.eirsteir.coffeewithme.social.web.request

import javax.validation.constraints.NotNull

data class FriendRequest(
    @get:NotNull(message = "Must be a valid id")
    val requesterId:  Long,
    @get:NotNull(message = "Must be a valid id")
    val addresseeId:  Long
)