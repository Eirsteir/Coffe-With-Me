package com.eirsteir.coffeewithme.notificationapi.dto;

import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.domain.NotificationType;
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

    private Long id;
    private Date timestamp;
    private Notification.UserDetails user;
    private Boolean seen;
    private NotificationType type;

}
