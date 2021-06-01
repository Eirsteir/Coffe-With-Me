package com.eirsteir.coffeewithme.social.config

import com.eirsteir.coffeewithme.social.repository.FriendshipRepository
import com.eirsteir.coffeewithme.social.repository.UniversityRepository
import com.eirsteir.coffeewithme.social.repository.UserRepository
import com.eirsteir.coffeewithme.social.service.AccountEventConsumer
import com.eirsteir.coffeewithme.social.service.user.UserService
import com.eirsteir.coffeewithme.social.service.user.UserServiceImpl
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfiguration {
    @Bean
    fun accountEventConsumer(
        domainEventPublisher: DomainEventPublisher,
        userRepository: UserRepository): AccountEventConsumer =
        AccountEventConsumer(userRepository, domainEventPublisher)

    @Bean
    fun userService(
        userRepository: UserRepository,
        universityRepository: UniversityRepository,
        friendshipRepository: FriendshipRepository,
        modelMapper: ModelMapper
    ): UserService = UserServiceImpl(
        userRepository, universityRepository, friendshipRepository, modelMapper
    )

    @Bean
    fun domainEventDispatcher(
        accountEventConsumer: AccountEventConsumer,
        domainEventDispatcherFactory: DomainEventDispatcherFactory
    ): DomainEventDispatcher = domainEventDispatcherFactory.make(
        "accountServiceEvents", accountEventConsumer.domainEventHandlers()
    )
}