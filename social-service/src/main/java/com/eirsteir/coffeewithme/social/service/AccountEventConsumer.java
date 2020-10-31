package com.eirsteir.coffeewithme.social.service;

import com.eirsteir.coffeewithme.commons.domain.account.AccountCreatedEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserAlreadyExistsEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserCreatedEvent;
import com.eirsteir.coffeewithme.social.domain.user.User;
import com.eirsteir.coffeewithme.social.repository.UserRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import java.util.Collections;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class AccountEventConsumer {

  @Autowired private UserRepository userRepository;

  @Autowired private DomainEventPublisher domainEventPublisher;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.authservice.domain.Account")
        .onEvent(AccountCreatedEvent.class, this::handleAccountCreatedEventHandler)
        .build();
  }

  private void handleAccountCreatedEventHandler(
      DomainEventEnvelope<AccountCreatedEvent> domainEventEnvelope) {
    Long accountId = Long.parseLong(domainEventEnvelope.getAggregateId());
    AccountCreatedEvent accountCreatedEvent = domainEventEnvelope.getEvent();

    Optional<User> possibleUser = userRepository.findById(accountId);

    log.info("[x] Handling event: {}", accountCreatedEvent);

    if (possibleUser.isEmpty()) {
      User user =
          User.builder()
              .id(accountId)
              .email(accountCreatedEvent.getEmail())
              .name(accountCreatedEvent.getName())
              .build();
      User registeredUser = userRepository.save(user);
      log.info("[x] Registered user: {}", registeredUser);

      UserCreatedEvent userCreatedEvent = new UserCreatedEvent(accountId);
      domainEventPublisher.publish(
          User.class, user.getId(), Collections.singletonList(userCreatedEvent));
      return;
    }

    log.info("[x] User already exists: {}", accountId);
    domainEventPublisher.publish(
        User.class, accountId, Collections.singletonList(new UserAlreadyExistsEvent(accountId)));
  }
}
