package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakServiceImpl;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeBreakConfiguration {

    @Bean
    public CoffeeBreakService coffeeBreakService(DomainEventPublisher domainEventPublisher,
                                                 CoffeeBreakRepository coffeeBreakRepository,
                                                 UserRepository userRepository,
                                                 CampusRepository campusRepository,
                                                 ModelMapper modelMapper) {
        return new CoffeeBreakServiceImpl(
                domainEventPublisher,
                coffeeBreakRepository,
                userRepository,
                campusRepository,
                modelMapper);
    }
}
