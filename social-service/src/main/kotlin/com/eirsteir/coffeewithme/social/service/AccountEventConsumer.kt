package com.eirsteir.coffeewithme.social.service

import com.eirsteir.coffeewithme.commons.domain.account.AccountCreatedEvent
import com.eirsteir.coffeewithme.commons.domain.user.UserAlreadyExistsEvent
import com.eirsteir.coffeewithme.commons.domain.user.UserCreatedEvent
import com.eirsteir.coffeewithme.social.domain.user.User
import com.eirsteir.coffeewithme.social.repository.UserRepository
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import mu.KotlinLogging
import java.util.*
import java.util.function.Consumer

private val logger = KotlinLogging.logger {  }

class AccountEventConsumer(
    private val userRepository: UserRepository,
    private val domainEventPublisher: DomainEventPublisher
) {

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.authservice.domain.Account"
        )
            .onEvent(
                AccountCreatedEvent::class.java
            ) { domainEventEnvelope: DomainEventEnvelope<AccountCreatedEvent> ->
                handleAccountCreatedEventHandler(domainEventEnvelope)
            }
            .build()
    }

    private fun handleAccountCreatedEventHandler(
        domainEventEnvelope: DomainEventEnvelope<AccountCreatedEvent>
    ) {
        val accountId: Long = domainEventEnvelope.aggregateId.toLong()
        val accountCreatedEvent: AccountCreatedEvent = domainEventEnvelope.event
        val possibleUser= userRepository.findById(accountId)

        logger.info("[x] Handling event: {}", accountCreatedEvent)
        if (possibleUser.isEmpty) {
            val user: User = User(
                id = accountId,
                email = accountCreatedEvent.email,
                name = accountCreatedEvent.name
            )
            val registeredUser = userRepository.save(user)
            logger.info("[x] Registered user: {}", registeredUser)
            val userCreatedEvent = UserCreatedEvent(accountId)
            domainEventPublisher.publish(
                User::class.java, user.id, listOf(userCreatedEvent)
            )
            return
        }
        logger.info("[x] User already exists: {}", accountId)
        domainEventPublisher.publish(
            User::class.java, accountId, listOf(UserAlreadyExistsEvent(accountId))
        )
    }
}