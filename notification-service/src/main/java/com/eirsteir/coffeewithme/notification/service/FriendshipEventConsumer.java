package com.eirsteir.coffeewithme.notification.service;

import com.eirsteir.coffeewithme.commons.domain.AbstractFriendshipEvent;
import com.eirsteir.coffeewithme.commons.domain.FriendRequestAcceptedEvent;
import com.eirsteir.coffeewithme.commons.domain.FriendRequestEvent;
import com.eirsteir.coffeewithme.commons.domain.NotificationType;
import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
public class FriendshipEventConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private DomainEventPublisher domainEventPublisher;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ModelMapper modelMapper;

    public DomainEventHandlers domainEventHandlers() {
        return DomainEventHandlersBuilder
                .forAggregateType("com.eirsteir.coffeewithme.social.domain.friendship.Friendship")
                .onEvent(FriendRequestEvent.class, this::handleFriendRequestEvent)
                .onEvent(FriendRequestAcceptedEvent.class, this::handleFriendRequestAcceptedEvent)
                .build();
    }

    private void handleFriendRequestEvent(DomainEventEnvelope<FriendRequestEvent> domainEventEnvelope) {
        FriendRequestEvent friendRequestEvent = domainEventEnvelope.getEvent();
        Notification notification = createNotification(friendRequestEvent);

        notificationRepository.save(notification);
        log.info("[x] Registered notification: {}", notification);

        convertAndSendToUser(notification);
    }

    private void handleFriendRequestAcceptedEvent(
            DomainEventEnvelope<FriendRequestAcceptedEvent> domainEventEnvelope) {
        FriendRequestAcceptedEvent friendRequestEvent = domainEventEnvelope.getEvent();
        Notification notification = createNotification(friendRequestEvent);

        notificationRepository.save(notification);
        log.info("[x] Registered notification: {}", notification);

        convertAndSendToUser(notification);
    }

    private Notification createNotification(AbstractFriendshipEvent friendshipEvent) {
        Long subjectId = friendshipEvent.getSubjectId();
        Notification.UserDetails userDetails = modelMapper.map(friendshipEvent.getUser(),
                                                               Notification.UserDetails.class);

        return Notification.builder()
                .subjectId(subjectId)
                .user(userDetails)
                .type(NotificationType.FRIEND_REQUEST)
                .build();
    }

    private void convertAndSendToUser(Notification notification) {
        log.info("[x] Sending notification: {}", notification);

        NotificationDto notificationDto = modelMapper.map(notification, NotificationDto.class);
        template.convertAndSendToUser(
                notification.getSubjectId().toString(),
                "/queue/notifications",
                notificationDto
        );
    }

}
