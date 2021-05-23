package com.eirsteir.coffeewithme.social.repository

import com.eirsteir.coffeewithme.social.domain.user.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*


interface UserRepository : JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    fun findByEmail(email: String): Optional<User>

    @Query("UPDATE User u SET u.lastLogin=:lastLogin WHERE u.email = ?#{ principal?.username }")
    @Modifying
    @Transactional
    fun updateLastLogin(@Param("lastLogin") lastLogin: Date) // TODO: 06.06.2020 query returning a user profile with friends count
}