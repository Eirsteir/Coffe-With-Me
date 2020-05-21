package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.util.MessageTemplateUtil;
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
    public void notify(Long toUserId, Long fromUserId, NotificationType type) {
        User toUser = userService.findUserById(toUserId);
        User fromUser = userService.findUserById(fromUserId);
        Notification notificationToSend = registerNotification(toUser, fromUser, type);

        log.debug("[x] Sending notification to user with id - {}: {}", toUserId, notificationToSend);
        NotificationDto notificationDto = modelMapper.map(notificationToSend, NotificationDto.class);

        sendToUser(notificationDto);
    }

    private void sendToUser(NotificationDto notificationDto) {
        template.convertAndSendToUser(
                notificationDto.getToUserId().toString(),
                "/queue/notifications",
                notificationDto
        );
    }

    private Notification registerNotification(User toUser, User fromUser, NotificationType type) {
        String message = getMessage(fromUser, toUser, type);

        Notification notification = Notification.builder()
                .message(message)
                .to(toUser)
                .build();

        log.debug("[x] Registering notification: {}", notification);
        return notificationRepository.save(notification);
    }

    private String getMessage(User from, User to, NotificationType type) {
        String messageTemplate = MessageTemplateUtil.getMessageTemplate(EntityType.FRIENDSHIP, type);

        if (type == NotificationType.ACCEPTED)
            return MessageTemplateUtil.format(messageTemplate, to.getName());

        return MessageTemplateUtil.format(messageTemplate, from.getName());
    }

    @Override
    public NotificationDto updateNotificationToRead(NotificationDto notificationDto) {
        Notification notificationToUpdate = notificationRepository.findById(notificationDto.getId())
                .orElseThrow(() -> CWMException.getException(NOTIFICATION,
                                                             ENTITY_NOT_FOUND,
                                                             notificationDto.getId().toString()));

        notificationToUpdate.setRead(true);

        return modelMapper.map(
                notificationRepository.save(notificationToUpdate), NotificationDto.class);
    }

    @Override
    public List<NotificationDto> findAllByUser(User user, Pageable pageable) {
        return notificationRepository.findAllByTo_IdOrderByCreatedDateTime(user.getId(), pageable)
                .stream()
                .map(notification -> modelMapper.map(notification, NotificationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public NotificationDto findByUserIdAndId(Long userId, Long id) {
        return null;
    }

}
