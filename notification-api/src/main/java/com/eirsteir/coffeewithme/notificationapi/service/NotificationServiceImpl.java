package com.eirsteir.coffeewithme.notificationapi.service;


import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.domain.NotificationType;
import com.eirsteir.coffeewithme.notificationapi.dto.NotificationDto;
import com.eirsteir.coffeewithme.notificationapi.exception.CWMException;
import com.eirsteir.coffeewithme.notificationapi.repository.NotificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.notificationapi.exception.EntityType.NOTIFICATION;
import static com.eirsteir.coffeewithme.notificationapi.exception.ExceptionType.ENTITY_NOT_FOUND;


@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void notify(Long subjectId, Notification.UserDetails currentUser, NotificationType type) {
        Notification notificationToSend = registerNotification(subjectId, currentUser, type);

        NotificationDto notificationDto = modelMapper.map(notificationToSend, NotificationDto.class);
        log.debug("[x] Sending notification to user with id - {}: {}", subjectId, notificationDto);

        sendToUser(notificationDto);
    }

    private void sendToUser(NotificationDto notificationDto) {
        template.convertAndSendToUser(
                notificationDto.getUser().getId().toString(),
                "/queue/notifications",
                notificationDto
        );
    }

    private Notification registerNotification(Long subjectId, Notification.UserDetails currentUser, NotificationType type) {
        Notification notification = Notification.builder()
                .subjectId(subjectId)
                .user(currentUser)
                .type(type)
                .build();

        Notification registeredNotification = notificationRepository.save(notification);
        log.debug("[x] Registered notification: {}", registeredNotification);

        return registeredNotification;
    }

    @Override
    public NotificationDto updateNotificationToRead(NotificationDto notificationDto) {
        Notification notificationToUpdate = notificationRepository.findById(notificationDto.getNotificationId())
                .orElseThrow(() -> CWMException.getException(NOTIFICATION,
                                                             ENTITY_NOT_FOUND,
                                                             notificationDto.getNotificationId().toString()));

        notificationToUpdate.setSeen(true);
        log.debug("[x] Updating notification: {}", notificationToUpdate);

        return modelMapper.map(
                notificationRepository.save(notificationToUpdate), NotificationDto.class);
    }

    @Override
    public List<NotificationDto> findAllByUserId(Long id, Pageable pageable) {
        return notificationRepository.findAllByUser_idOrderByTimestamp(id, pageable)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

}
