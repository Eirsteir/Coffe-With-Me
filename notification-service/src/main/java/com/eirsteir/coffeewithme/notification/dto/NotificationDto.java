package com.eirsteir.coffeewithme.notification.dto;

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.notification.domain.Notification;
import java.util.Date;
import lombok.*;
import lombok.experimental.Accessors;

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
