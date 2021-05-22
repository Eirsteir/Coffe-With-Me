package com.eirsteir.coffeewithme.authservice.repository

import com.eirsteir.coffeewithme.authservice.domain.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, Long> {
    fun findByEmail(email: String): Account?
}