package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    List<NotificationDto> getNotifications(Pageable pageable,
                                           @AuthenticationPrincipal UserPrincipalImpl principal) {
        List<NotificationDto> notifications = notificationService.findAllByUser(principal.getUser(), pageable);

        if (notifications.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with email " + principal.getEmail() + " has no notifications");

        return notifications;
    }

    @PutMapping
    NotificationDto updateNotificationToRead(@RequestBody @Valid NotificationDto notificationDto,
                                       @AuthenticationPrincipal UserPrincipalImpl principal) {
        validateNotificationUpdate(notificationDto, principal);

        return notificationService.updateNotificationToRead(notificationDto);
    }

    private void validateNotificationUpdate(NotificationDto notificationDto, UserPrincipalImpl principal) {
        if (notificationDto.getToUserId().equals(principal.getUser().getId()))
            return;

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Notification does not belong to current user");
    }
}
