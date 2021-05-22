package com.eirsteir.coffeewithme.authservice.service

import com.eirsteir.coffeewithme.authservice.domain.Role
import com.eirsteir.coffeewithme.authservice.domain.RoleType

interface RoleService {
    fun getOrCreateRole(type: RoleType): Role
}