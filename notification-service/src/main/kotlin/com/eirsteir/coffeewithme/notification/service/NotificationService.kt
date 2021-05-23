package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import org.springframework.data.domain.Pageable

interface NotificationService {
    fun updateNotificationToRead(notificationDto: NotificationDto): NotificationDto
    fun findAllByUserId(id: Long, pageable: Pageable): MutableList<NotificationDto>
}