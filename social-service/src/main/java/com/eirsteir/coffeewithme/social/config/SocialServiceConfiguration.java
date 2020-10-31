package com.eirsteir.coffeewithme.social.config;

import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.eirsteir.coffeewithme.social.repository")
@EnableAutoConfiguration
@Import({
  UserConfiguration.class,
  FriendshipConfiguration.class,
  CoffeeBreakConfiguration.class,
  ModelMapperConfig.class,
  TramJdbcKafkaConfiguration.class,
  TramEventsPublisherConfiguration.class,
  TramEventSubscriberConfiguration.class,
  OptimisticLockingDecoratorConfiguration.class
})
public class SocialServiceConfiguration {}
