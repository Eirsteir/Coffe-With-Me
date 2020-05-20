package com.eirsteir.coffeewithme.service.notification;

import com.eirsteir.coffeewithme.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NotificationService {

    void notify(Long userId, Long fromUserId, NotificationType type);

    NotificationDto updateNotificationToRead(NotificationDto notificationDto);

    List<NotificationDto> findAllByUser(User user, Pageable pageable);

    NotificationDto findByUserIdAndId(Long userId, Long id);

}
