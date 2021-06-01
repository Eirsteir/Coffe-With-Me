package com.eirsteir.coffeewithme.commons.domain.account

data class AccountCreatedEvent(
    val accountId: Long,
    val email: String,
    val name: String,
) : AccountEvent