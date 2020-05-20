package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.service.user.UserService;
import com.eirsteir.coffeewithme.util.MessageTemplateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void notify(Long toUserId, NotificationType type) {
        Notification notificationToSend = registerNotification(toUserId, type);

        log.debug("[x] Sending notification to user with id - {}: {}", toUserId, notificationToSend);
        sendToUser(notificationToSend);
    }

    private void sendToUser(Notification notification) {
        template.convertAndSendToUser(
                notification.getUser().getId().toString(),
                "/queue/notifications",
                notification
        );
    }

    private Notification registerNotification(Long toUserId, NotificationType type) {
        User toUser = userService.findUserById(toUserId);
        Notification notification = Notification.builder()
                .message(MessageTemplateUtil.getMessageTemplate(type))
                .user(toUser)
                .build();

        log.debug("[x] Registering notification: {}", notification);
        return notificationRepository.save(notification);
    }

    @Override
    public NotificationDto updateNotification(NotificationDto notificationDto) {
        return null;
    }

    @Override
    public NotificationDto findByUserId(Long userId) {
        return null;
    }

    @Override
    public NotificationDto findByUserIdAndId(Long userId, Long id) {
        return null;
    }

}
