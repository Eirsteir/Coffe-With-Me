package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.dto.NotificationDto;

public interface NotificationService {

    void notify(Long userId, NotificationType type);

    NotificationDto updateNotification(NotificationDto notificationDto);

    NotificationDto findByUserId(Long userId);

    NotificationDto findByUserIdAndId(Long userId, Long id);

}
