package com.eirsteir.coffeewithme.social.web.request;

import com.eirsteir.coffeewithme.social.domain.notification.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

    private Long subjectId;
    private NotificationType type;
    private Long userId;
    private String name;
    private String username;

}
