package com.eirsteir.coffeewithme.notification.dto

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType
import com.eirsteir.coffeewithme.notification.domain.Notification
import lombok.*
import lombok.experimental.Accessors
import java.util.*


data class NotificationDto(
    val notificationId: Long,
    val timestamp: Date? = null,
    val user: Notification.UserDetails? = null,
    val type: NotificationType? = null,
    val seen: Boolean? = null
)