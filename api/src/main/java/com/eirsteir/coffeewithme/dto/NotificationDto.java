package com.eirsteir.coffeewithme.dto;

import com.eirsteir.coffeewithme.domain.notification.NotificationType;
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
    private UserDto user;
    private Boolean seen;
    private NotificationType type;
    private Boolean requestedByViewer;

}
