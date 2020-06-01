package com.eirsteir.coffeewithme.notification.dto;

import com.eirsteir.coffeewithme.notification.domain.Notification;
import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Accessors(chain = true)
public class NotificationDto {

    private Long notificationId;
    private Date timestamp;
    private Notification.UserDetails user;
    private Boolean seen;
    private NotificationType type;

}
