package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.notification.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return null;
    }

    NotificationDto updateNotification(@RequestBody @Valid NotificationDto notificationDto,
                                       @AuthenticationPrincipal UserPrincipalImpl principal) {
        return null;
    }
}
