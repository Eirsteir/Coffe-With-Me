package com.eirsteir.coffeewithme.authservice.repository

import com.eirsteir.coffeewithme.authservice.domain.Role
import com.eirsteir.coffeewithme.authservice.domain.RoleType
import org.springframework.data.repository.CrudRepository
import java.util.*

interface RoleRepository : CrudRepository<Role, Long> {
    fun findByType(type: RoleType): Role?
}