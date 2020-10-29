package com.eirsteir.coffeewithme.social.config;


import com.eirsteir.coffeewithme.social.repository.FriendshipRepository;
import com.eirsteir.coffeewithme.social.repository.UniversityRepository;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import com.eirsteir.coffeewithme.social.service.AccountEventConsumer;
import com.eirsteir.coffeewithme.social.service.user.UserService;
import com.eirsteir.coffeewithme.social.service.user.UserServiceImpl;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfiguration {

    @Bean
    public AccountEventConsumer accountEventConsumer() {
        return new AccountEventConsumer();
    }

    @Bean
    public UserService userService(UserRepository userRepository,
                                   UniversityRepository universityRepository,
                                   FriendshipRepository friendshipRepository,
                                   ModelMapper modelMapper) {
        return new UserServiceImpl(userRepository,
                                   universityRepository,
                                   friendshipRepository,
                                   modelMapper);
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(AccountEventConsumer accountEventConsumer,
                                                       DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make(
                "accountServiceEvents", accountEventConsumer.domainEventHandlers()
        );
    }

}

