package com.eirsteir.coffeewithme.notification.service;

import com.eirsteir.coffeewithme.commons.domain.coffeebreak.CoffeeBreakCreatedEvent;
import com.eirsteir.coffeewithme.commons.domain.user.UserDetails;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

public class CoffeeBreakEventConsumer extends EventConsumer {

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.social.domain.coffeebreak.CoffeeBreak")
        .onEvent(CoffeeBreakCreatedEvent.class, this::handleCoffeeBreakCreatedEvent)
        .build();
  }

  private void handleCoffeeBreakCreatedEvent(
      DomainEventEnvelope<CoffeeBreakCreatedEvent> domainEventEnvelope) {
    CoffeeBreakCreatedEvent coffeeBreakCreatedEvent = domainEventEnvelope.getEvent();
    for (UserDetails userDetails : coffeeBreakCreatedEvent.getCoffeeBreakDetails().getAddressees())
      super.handleEvent(coffeeBreakCreatedEvent);
  }
}
