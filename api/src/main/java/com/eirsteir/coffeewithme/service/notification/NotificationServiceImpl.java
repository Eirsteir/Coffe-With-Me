package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.eirsteir.coffeewithme.exception.EntityType.NOTIFICATION;
import static com.eirsteir.coffeewithme.exception.ExceptionType.ENTITY_NOT_FOUND;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void notify(Long toUserId, User currentUser, NotificationType type) {
        User toUser = userService.findUserById(toUserId);
        Notification notificationToSend = registerNotification(toUser, currentUser, type);

        NotificationDto notificationDto = modelMapper.map(notificationToSend, NotificationDto.class);
        log.debug("[x] Sending notification to user with id - {}: {}", toUserId, notificationDto);

        sendToUser(notificationDto);
    }

    private void sendToUser(NotificationDto notificationDto) {
        template.convertAndSendToUser(
                notificationDto.getUser().getId().toString(),
                "/queue/notifications",
                notificationDto
        );
    }

    private Notification registerNotification(User toUser, User currentUser, NotificationType type) {
        boolean requestedByViewer = toUser.equals(currentUser);

        Notification notification = Notification.builder()
                .user(toUser)
                .type(type)
                .requestedByViewer(requestedByViewer)
                .build();

        Notification registeredNotification = notificationRepository.save(notification);
        log.debug("[x] Registered notification: {}", registeredNotification);

        return registeredNotification;
    }

    @Override
    public NotificationDto updateNotificationToRead(NotificationDto notificationDto) {
        Notification notificationToUpdate = notificationRepository.findById(notificationDto.getId())
                .orElseThrow(() -> CWMException.getException(NOTIFICATION,
                                                             ENTITY_NOT_FOUND,
                                                             notificationDto.getId().toString()));

        notificationToUpdate.setSeen(true);
        log.debug("[x] Updating notification: {}", notificationToUpdate);

        return modelMapper.map(
                notificationRepository.save(notificationToUpdate), NotificationDto.class);
    }

    @Override
    public List<NotificationDto> findAllByUser(User user, Pageable pageable) {
        return notificationRepository.findAllByUserOrderByTimestamp(user, pageable)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

}
