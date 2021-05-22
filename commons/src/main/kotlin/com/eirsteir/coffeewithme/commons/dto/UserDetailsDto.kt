package com.eirsteir.coffeewithme.commons.dto

data class UserDetailsDto(
    val id: Long? = null,
    val email: String? = null,
    val nickname: String? = null,
    val name: String? = null,
    val isFriend: Boolean? = null,
    val friendsCount: Int? = null,
)