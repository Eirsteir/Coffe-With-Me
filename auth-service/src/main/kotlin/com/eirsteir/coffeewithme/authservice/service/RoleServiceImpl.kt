package com.eirsteir.coffeewithme.authservice.service

import com.eirsteir.coffeewithme.authservice.domain.Role
import com.eirsteir.coffeewithme.authservice.domain.RoleType
import com.eirsteir.coffeewithme.authservice.repository.RoleRepository
import mu.KotlinLogging
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {}

@Service
@Transactional
class RoleServiceImpl(private val repository: RoleRepository) : RoleService {

    override fun getOrCreateRole(type: RoleType): Role {
        return repository.findByType(type)
            ?: run {
                val savedRole = repository.save(Role(type = type))
                logger.info("[x] Role not found, created new: {}", savedRole)
                savedRole
        }
    }
}