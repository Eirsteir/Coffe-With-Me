package com.eirsteir.coffeewithme.social.dto;

import com.eirsteir.coffeewithme.social.domain.notification.NotificationType;
import com.eirsteir.coffeewithme.commons.dto.UserDetails;
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
    private UserDetails user;
    private Boolean seen;
    private NotificationType type;

}