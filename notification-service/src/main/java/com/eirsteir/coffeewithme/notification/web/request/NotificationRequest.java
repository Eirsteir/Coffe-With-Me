package com.eirsteir.coffeewithme.notification.web.request;

import com.eirsteir.coffeewithme.notification.domain.NotificationType;
import lombok.Getter;

@Getter
public class NotificationRequest {

    private Long subjectId;
    private NotificationType type;
    private Long userId;
    private String name;
    private String username;

}
