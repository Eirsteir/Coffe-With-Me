package com.eirsteir.coffeewithme.notificationapi.web.api;


import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.dto.NotificationDto;
import com.eirsteir.coffeewithme.notificationapi.service.NotificationService;
import com.eirsteir.coffeewithme.notificationapi.web.api.request.NotificationRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.function.ServerRequest;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void notify(@RequestBody NotificationRequest notificationRequest, @CookieValue("SESSION") String session) {
        // @CookieValue("SESSION") String session

        log.debug("[x] Received request: {}", notificationRequest);

        Notification.UserDetails userDetails = Notification.UserDetails.builder()
                .id(notificationRequest.getUserId())
                .name(notificationRequest.getName())
                .username(notificationRequest.getUsername())
                .build();

        service.notify(notificationRequest.getSubjectId(), userDetails, notificationRequest.getType());
    }

    @GetMapping("/users/{id}")
    List<NotificationDto> getNotifications(@PathVariable Long id, Pageable pageable, @CookieValue("SESSION") String session, Authentication authentication) {
        log.debug("[x] Received request with session: {}", session);
        log.debug("[x] Received request with authentication: {}", authentication);

        List<NotificationDto> notifications = service.findAllByUserId(id, pageable);

        if (notifications.isEmpty())
            throw new ResponseStatusException(
                    HttpStatus.NO_CONTENT, "User with id " + id + " has no notifications");

        return notifications;
    }

    @PutMapping("/users/{id}")
    NotificationDto updateNotificationToRead(@PathVariable Long id, @RequestBody NotificationDto notificationDto, ServerRequest request) {
        log.debug("[x] Received request: {}", request);

        validateNotificationUpdate(notificationDto, id);

        return service.updateNotificationToRead(notificationDto);
    }

    private void validateNotificationUpdate(NotificationDto notificationDto, Long userId) {
        if (notificationDto.getUser().getId().equals(userId))
            return;

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Notification does not belong to current user");
    }
}
