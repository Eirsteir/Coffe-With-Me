package com.eirsteir.coffeewithme.notification.service;


import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.commons.domain.NotificationType;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NotificationService {

    void notify(Long subjectId, Notification.UserDetails currentUser, NotificationType type);

    NotificationDto updateNotificationToRead(NotificationDto notificationDto);

    List<NotificationDto> findAllByUserId(Long id, Pageable pageable);

}
