package com.eirsteir.coffeewithme.notification.service;

import com.eirsteir.coffeewithme.commons.domain.AbstractEntityNotificationEvent;
import com.eirsteir.coffeewithme.commons.domain.NotificationType;
import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Slf4j
public abstract class EventConsumer {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ModelMapper modelMapper;

    protected void handleEvent(AbstractEntityNotificationEvent domainEvent) {
        Notification notification = createNotification(domainEvent);

        notificationRepository.save(notification);
        log.info("[x] Registered notification: {}", notification);

        convertAndSendToUser(notification);
    }

    private Notification createNotification(AbstractEntityNotificationEvent friendshipEvent) {
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
