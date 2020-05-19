package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.dto.NotificationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void notify(NotificationDto notificationDto, String email) {
        log.debug("Sending notification to user with email - {}: {}", email, notificationDto);
        template.convertAndSendToUser(
                email,
                "/queue/notify",
                notificationDto
        );

        log.debug("Sending notification to user with id - {}: {}", notificationDto.getUserId(), notificationDto);
        template.convertAndSendToUser(
                notificationDto.getUserId().toString(),
                "/queue/notify",
                notificationDto
        );
    }

    @Override
    public NotificationDto registerNotification(NotificationDto notificationDto) {
        return null;
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
