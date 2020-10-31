package com.eirsteir.coffeewithme.notification.service;

import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestAcceptedEvent;
import com.eirsteir.coffeewithme.commons.domain.friendship.FriendRequestEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;

public class FriendshipEventConsumer extends EventConsumer {

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder.forAggregateType(
            "com.eirsteir.coffeewithme.social.domain.friendship.Friendship")
        .onEvent(FriendRequestEvent.class, this::handleFriendRequestEvent)
        .onEvent(FriendRequestAcceptedEvent.class, this::handleFriendRequestAcceptedEvent)
        .build();
  }

  private void handleFriendRequestEvent(
      DomainEventEnvelope<FriendRequestEvent> domainEventEnvelope) {
    FriendRequestEvent friendRequestEvent = domainEventEnvelope.getEvent();
    super.handleEvent(friendRequestEvent);
  }

  private void handleFriendRequestAcceptedEvent(
      DomainEventEnvelope<FriendRequestAcceptedEvent> domainEventEnvelope) {
    FriendRequestAcceptedEvent friendRequestEvent = domainEventEnvelope.getEvent();
    super.handleEvent(friendRequestEvent);
  }
}
