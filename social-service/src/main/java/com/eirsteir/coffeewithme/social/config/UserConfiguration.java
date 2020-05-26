package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.service.AccountEventConsumer;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.service.user.UserServiceImpl;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
public class UserConfiguration {

    @Bean
    public AccountEventConsumer accountEventConsumer() {
        return new AccountEventConsumer();
    }

    @Bean
    public UserService userService() {
        return new UserServiceImpl();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(AccountEventConsumer accountEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make(
                "AccountServiceEvents", accountEventConsumer.domainEventHandlers()
        );
    }

}
