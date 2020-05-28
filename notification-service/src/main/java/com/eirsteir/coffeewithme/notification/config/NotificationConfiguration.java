package com.eirsteir.coffeewithme.notification.config;

import com.eirsteir.coffeewithme.notification.service.FriendshipEventConsumer;
import com.eirsteir.coffeewithme.notification.service.NotificationService;
import com.eirsteir.coffeewithme.notification.service.NotificationServiceImpl;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class,
        OptimisticLockingDecoratorConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
public class NotificationConfiguration {

    @Bean
    public FriendshipEventConsumer friendshipEventConsumer() {
        return new FriendshipEventConsumer();
    }

    @Bean
    public NotificationService notificationService() {
        return new NotificationServiceImpl();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(FriendshipEventConsumer friendshipEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make(
                "friendshipServiceEvent", friendshipEventConsumer.domainEventHandlers()
        );
    }
}
