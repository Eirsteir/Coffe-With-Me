package com.eirsteir.coffeewithme.social.config

import com.eirsteir.coffeewithme.social.repository.FriendshipRepository
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipServiceImpl
import com.eirsteir.coffeewithme.social.service.user.UserService
import io.eventuate.tram.events.publisher.DomainEventPublisher
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FriendshipConfiguration {
    @Bean
    fun friendshipService(
        domainEventPublisher: DomainEventPublisher,
        friendshipRepository: FriendshipRepository,
        userService: UserService,
        modelMapper: ModelMapper
    ): FriendshipService = FriendshipServiceImpl(
        domainEventPublisher, userService, friendshipRepository, modelMapper
    )
}