package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakServiceImpl;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({TramJdbcKafkaConfiguration.class,
        TramEventsPublisherConfiguration.class,
        TramEventSubscriberConfiguration.class})
public class CoffeeBreakConfiguration {

    @Bean
    public CoffeeBreakService coffeeBreakService() {
        return new CoffeeBreakServiceImpl();
    }
}
