package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.repository.CampusRepository;
import com.eirsteir.coffeewithme.social.repository.CoffeeBreakRepository;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakService;
import com.eirsteir.coffeewithme.social.service.coffeebreak.CoffeeBreakServiceImpl;
import com.eirsteir.coffeewithme.social.service.friendship.FriendshipService;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CoffeeBreakConfiguration {


    @Bean
    public CoffeeBreakService coffeeBreakService(DomainEventPublisher domainEventPublisher,
                                                 CoffeeBreakRepository coffeeBreakRepository,
                                                 FriendshipService friendshipService,
                                                 UserService userService,
                                                 CampusRepository campusRepository) {
        return new CoffeeBreakServiceImpl(
                domainEventPublisher,
                coffeeBreakRepository,
                friendshipService,
                userService,
                campusRepository);
    }
}
