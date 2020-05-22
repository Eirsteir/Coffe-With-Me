package com.eirsteir.coffeewithme.notificationapi.service;


import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.domain.NotificationType;
import com.eirsteir.coffeewithme.notificationapi.dto.NotificationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    void notify(Long subjectId, Notification.UserDetails currentUser, NotificationType type);

    NotificationDto updateNotificationToRead(NotificationDto notificationDto);

    List<NotificationDto> findAllByUserId(Long id, Pageable pageable);

}
