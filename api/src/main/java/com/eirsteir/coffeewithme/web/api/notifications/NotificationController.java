package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.security.UserPrincipalImpl;
import com.eirsteir.coffeewithme.service.notification.NotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/notifications")
@Api(tags = {"Notifications"})
@SwaggerDefinition(tags = {
        @Tag(name = "Notifications", description = "Notifications management operations for this application")
})
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @ApiOperation("Get notifications for the currently logged in user")
    List<NotificationDto> getNotifications(Pageable pageable,
                                           @AuthenticationPrincipal UserPrincipalImpl principal) {
        return null;
    }

    @PutMapping
    @ApiOperation("Update notification")
    NotificationDto updateNotification(@RequestBody @Valid NotificationDto notificationDto,
                                       @AuthenticationPrincipal UserPrincipalImpl principal) {
        return null;
    }
}
