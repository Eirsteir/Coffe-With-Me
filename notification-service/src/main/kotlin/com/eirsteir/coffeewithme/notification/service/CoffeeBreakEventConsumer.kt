package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakCreatedEvent
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import io.eventuate.tram.events.subscriber.DomainEventEnvelope
import io.eventuate.tram.events.subscriber.DomainEventHandlers
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder
import org.modelmapper.ModelMapper
import org.springframework.messaging.simp.SimpMessagingTemplate

class CoffeeBreakEventConsumer(notificationRepository: NotificationRepository, template: SimpMessagingTemplate,
                               modelMapper: ModelMapper
)
    : EventConsumer(notificationRepository, template, modelMapper) {

    fun domainEventHandlers(): DomainEventHandlers {
        return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak"
        )
            .onEvent(CoffeeBreakCreatedEvent::class.java) { domainEventEnvelope: DomainEventEnvelope<CoffeeBreakCreatedEvent?>? ->
                handleCoffeeBreakCreatedEvent(
                    domainEventEnvelope
                )
            }
            .build()
    }

    private fun handleCoffeeBreakCreatedEvent(
        domainEventEnvelope: DomainEventEnvelope<CoffeeBreakCreatedEvent?>?
    ) {
        val coffeeBreakCreatedEvent = domainEventEnvelope?.event
        for (user in coffeeBreakCreatedEvent?.coffeeBreakDetails?.addressees!!)
            super.handleEvent(
            coffeeBreakCreatedEvent
        )
    }
}