package com.eirsteir.coffeewithme.authservice.service

import com.eirsteir.coffeewithme.authservice.domain.Account
import com.eirsteir.coffeewithme.authservice.domain.RoleType
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository
import com.eirsteir.coffeewithme.authservice.web.request.RegistrationRequest
import com.eirsteir.coffeewithme.commons.exception.CWMException
import com.eirsteir.coffeewithme.commons.exception.EntityType
import com.eirsteir.coffeewithme.commons.exception.ExceptionType
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.publisher.ResultWithEvents
import mu.KotlinLogging
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


private val logger = KotlinLogging.logger {}

@Service
@Transactional
class AccountServiceImpl(
    private val domainEventPublisher: DomainEventPublisher,
    private val accountRepository: AccountRepository,
    private val roleService: RoleService,
    private val encoder: BCryptPasswordEncoder
) : AccountService {

    override fun registerAccount(registrationRequest: RegistrationRequest): Account {
        val user = accountRepository.findByEmail(registrationRequest.email)

        if (user != null) throw CWMException.getException(
            EntityType.ACCOUNT, ExceptionType.DUPLICATE_ENTITY, user.id.toString()
        )

        var account = createAccount(registrationRequest)
        logger.debug("[x] Created account {}", account)

        account = accountRepository.save(account)
        logger.info("[x] Registered account: {}", account)

        val accountWithEvents: ResultWithEvents<Account> = Account.createAccount(account)
        domainEventPublisher.publish(Account::class.java, account.id, accountWithEvents.events)

        logger.info("[x] Publishing {} to {}", Account::class.java, accountWithEvents)

        return account
    }

    private fun createAccount(registrationRequest: RegistrationRequest): Account {
        val basicRole = roleService.getOrCreateRole(RoleType.ROLE_USER)
        return Account(
            email = registrationRequest.email,
            name = registrationRequest.name,
            roles = setOf(basicRole),
            password = encoder.encode(registrationRequest.password)
        )
    }
}