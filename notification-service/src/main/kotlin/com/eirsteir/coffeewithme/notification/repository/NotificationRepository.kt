package com.eirsteir.coffeewithme.notification.repository

import com.eirsteir.coffeewithme.notification.domain.Notification
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface NotificationRepository : JpaRepository<Notification, Long> {
    fun findAllByUser_idOrderByTimestamp(userId: Long, pageable: Pageable): List<Notification>
}