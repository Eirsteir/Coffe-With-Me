package com.eirsteir.coffeewithme.notification.service;


import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface NotificationService {

    NotificationDto updateNotificationToRead(NotificationDto notificationDto);

    List<NotificationDto> findAllByUserId(Long id, Pageable pageable);

}
