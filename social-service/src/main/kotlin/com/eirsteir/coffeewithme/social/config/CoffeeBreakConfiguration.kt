package com.eirsteir.coffeewithme.social.config

import com.eirsteir.coffeewithme.social.repository.CampusRepository
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository
import com.eirsteir.coffeewithme.social.repository.UserRepository
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakServiceImpl
import io.eventuate.tram.events.publisher.DomainEventPublisher
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration
import io.eventuate.tram.spring.optimisticlocking.OptimisticLockingDecoratorConfiguration
import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration

class CoffeeBreakConfiguration {
    @Bean
    fun coffeeBreakService(
        domainEventPublisher: DomainEventPublisher,
        coffeeBreakRepository: CoffeeBreakRepository,
        userRepository: UserRepository,
        campusRepository: CampusRepository,
        modelMapper: ModelMapper
    ): CoffeeBreakService = CoffeeBreakServiceImpl(
        domainEventPublisher, coffeeBreakRepository, userRepository, campusRepository, modelMapper
    )
}