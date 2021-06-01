package com.eirsteir.coffeewithme.authservice.config

import com.eirsteir.coffeewithme.authservice.domain.Account
import com.eirsteir.coffeewithme.authservice.domain.RoleType
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository
import com.eirsteir.coffeewithme.authservice.repository.RoleRepository
import com.eirsteir.coffeewithme.authservice.service.RoleService
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.*

private val logger = KotlinLogging.logger {}

@Component
class SetupDataLoader(
    private val accountRepository: AccountRepository,
    private val roleRepository: RoleRepository,
    private val roleService: RoleService,
    private val encoder: BCryptPasswordEncoder
) : ApplicationListener<ContextRefreshedEvent?> {
    private var alreadySetup = false

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        if (alreadySetup)
            return

        val adminRole = roleService.getOrCreateRole(RoleType.ROLE_ADMIN)
        val basicRole = roleService.getOrCreateRole(RoleType.ROLE_USER)
        val readerRole = roleService.getOrCreateRole(RoleType.ROLE_READER)
        val writerRole = roleService.getOrCreateRole(RoleType.ROLE_WRITER)

        var account = Account(
            email = "admin@test.com",
            password = encoder.encode("admin"),
            roles = setOf(adminRole, readerRole, writerRole)
        )

        logger.info("[x] Preloading {}", accountRepository.save(account))
        account = Account(
            email = "user@test.com",
            password = encoder.encode("password"),
            roles = setOf(basicRole)
        )
        logger.info("[x] Preloading {}", accountRepository.save(account))

        account = Account(
            email = "audit@test.com",
            password = encoder.encode("audit"),
            roles = setOf(adminRole, readerRole)
        )
        logger.info("[x] Preloading {}", accountRepository.save(account))

        alreadySetup = true
    }
}