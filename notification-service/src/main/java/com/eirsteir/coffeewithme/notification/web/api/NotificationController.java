package com.eirsteir.coffeewithme.notification.web.api;

import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.eirsteir.coffeewithme.notification.dto.NotificationDto;
import com.eirsteir.coffeewithme.notification.service.NotificationService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

  @Autowired private NotificationService notificationService;

  @GetMapping("/users/{id}")
  List<NotificationDto> getNotifications(
      @PathVariable Long id,
      Pageable pageable,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    log.debug("[x] Received request with principal: {}", principal);

    List<NotificationDto> notifications = notificationService.findAllByUserId(id, pageable);

    if (notifications.isEmpty())
      throw new ResponseStatusException(
          HttpStatus.NO_CONTENT, "User with id " + id + " has no notifications");

    return notifications;
  }

  @PutMapping("/users/{id}")
  NotificationDto updateNotificationToRead(
      @PathVariable Long id,
      @RequestBody NotificationDto notificationDto,
      @AuthenticationPrincipal UserDetailsImpl principal) {
    log.debug("[x] Received request with principal: {}", principal);

    validateNotificationUpdate(notificationDto, principal.getId());

    return notificationService.updateNotificationToRead(notificationDto);
  }

  private void validateNotificationUpdate(NotificationDto notificationDto, Long userId) {
    if (notificationDto.getUser().getId().equals(userId)) return;

    throw new ResponseStatusException(
        HttpStatus.BAD_REQUEST, "Notification does not belong to current user");
  }
}
