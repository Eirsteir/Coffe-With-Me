package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.dto.NotificationDto;

public interface NotificationService {

    void notify(NotificationDto notificationDto, String email);

    NotificationDto registerNotification(NotificationDto notificationDto);

    NotificationDto updateNotification(NotificationDto notificationDto);

    NotificationDto findByUserId(Long userId);

    NotificationDto findByUserIdAndId(Long userId, Long id);

}
