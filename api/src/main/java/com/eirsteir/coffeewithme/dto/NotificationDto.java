package com.eirsteir.coffeewithme.dto;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String message;
    private Date createdDateTime;
    private Boolean isRead;
    private Long toUserId;
    private String path;

}
