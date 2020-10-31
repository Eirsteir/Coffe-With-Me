package com.eirsteir.coffeewithme.notification.service;

import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface NotificationService {

  NotificationDto updateNotificationToRead(NotificationDto notificationDto);

  List<NotificationDto> findAllByUserId(Long id, Pageable pageable);
}
