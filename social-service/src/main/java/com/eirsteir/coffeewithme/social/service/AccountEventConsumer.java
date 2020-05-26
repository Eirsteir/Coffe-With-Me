package com.eirsteir.coffeewithme.social.service;

import com.eirsteir.coffeewithme.commons.domain.AccountCreatedEvent;
import com.eirsteir.coffeewithme.commons.domain.UserAlreadyExistsEvent;
import com.eirsteir.coffeewithme.commons.domain.UserCreatedEvent;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.Optional;

@Slf4j
public class AccountEventConsumer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.eirsteir.coffeewithme.authservice.domain.account")
                .onEvent(AccountCreatedEvent.class, this::handleAccountCreatedEventHandler)
                .build();
    }

    private void handleAccountCreatedEventHandler(DomainEventEnvelope<AccountCreatedEvent> domainEventEnvelope) {
        Long accountId = Long.parseLong(domainEventEnvelope.getAggregateId());
        AccountCreatedEvent accountCreatedEvent = domainEventEnvelope.getEvent();

        Optional<User> possibleUser = userRepository.findById(accountId);

        log.info("[x] Handling event: {}", accountCreatedEvent);

        if (possibleUser.isPresent()) {
            log.info("[x] User already exists: {}", accountId);
            domainEventPublisher.publish(User.class,
                                         accountId,
                                         Collections.singletonList(new UserAlreadyExistsEvent(accountId)));
            return;
        }

        User user = possibleUser.get();

        UserCreatedEvent userCreatedEvent = new UserCreatedEvent(accountId);
        domainEventPublisher.publish(User.class,
                                     user.getId(),
                                     Collections.singletonList(userCreatedEvent));
    }

}
