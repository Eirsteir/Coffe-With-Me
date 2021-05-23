package com.eirsteir.coffeewithme.notification.config

import com.eirsteir.coffeewithme.notification.service.FriendshipEventConsumer
import io.eventuate.tram.events.subscriber.DomainEventDispatcher
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@Import(
    TramJdbcKafkaConfiguration::class,
    TramEventsPublisherConfiguration::class,
    TramEventSubscriberConfiguration::class,
    OptimisticLockingDecoratorConfiguration::class
)
class NotificationConfiguration {


    @Bean
    fun domainEventDispatcher(
        friendshipEventConsumer: FriendshipEventConsumer,
        domainEventDispatcherFactory: DomainEventDispatcherFactory
    ): DomainEventDispatcher? {
        return domainEventDispatcherFactory.make(
            "friendshipServiceEvent", friendshipEventConsumer.domainEventHandlers()
        )
    }
}