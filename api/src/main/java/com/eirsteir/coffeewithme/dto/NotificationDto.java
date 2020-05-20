package com.eirsteir.coffeewithme.dto;

import lombok.*;

import java.util.Date;

@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String message;
    private Date createdDateTime;
    private boolean isRead;
    private Long userId;

}