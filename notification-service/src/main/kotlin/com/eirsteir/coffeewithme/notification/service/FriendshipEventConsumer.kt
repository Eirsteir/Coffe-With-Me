package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestAcceptedEvent
import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestEvent
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import org.modelmapper.ModelMapper
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Component

@Component
class FriendshipEventConsumer(
    notificationRepository: NotificationRepository,
    template: SimpMessagingTemplate,
    modelMapper: ModelMapper
) : EventConsumer(notificationRepository, template, modelMapper) {

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.social.domain.friendship.Friendship"
        )
            .onEvent(FriendRequestEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<FriendRequestEvent> ->
                handleFriendRequestEvent(
                    domainEventEnvelope
                )
            }
            .onEvent(FriendRequestAcceptedEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<FriendRequestAcceptedEvent> ->
                handleFriendRequestAcceptedEvent(
                    domainEventEnvelope
                )
            }
            .build()
    }

    private fun handleFriendRequestEvent(
        domainEventEnvelope: DomainEventEnvelope<FriendRequestEvent>
    ) {
        val friendRequestEvent = domainEventEnvelope.event
        super.handleEvent(friendRequestEvent)
    }

    private fun handleFriendRequestAcceptedEvent(
        domainEventEnvelope: DomainEventEnvelope<FriendRequestAcceptedEvent>
    ) {
        val friendRequestEvent = domainEventEnvelope.event
        super.handleEvent(friendRequestEvent)
    }
}