package com.eirsteir.coffeewithme.notification.web.api

import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import com.eirsteir.coffeewithme.notification.service.NotificationService
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


private val logger = KotlinLogging.logger { }

@RestController
@RequestMapping("/notifications")
class NotificationController(private val notificationService: NotificationService) {

    @GetMapping("/users/{id}")
    fun getNotifications(
        @PathVariable id: Long,
        pageable: Pageable,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): MutableList<NotificationDto> {
        logger.debug("[x] Received request with principal: {}", principal)
        val notifications = notificationService.findAllByUserId(id, pageable)

        if (notifications.isEmpty())
            throw ResponseStatusException(
            HttpStatus.NO_CONTENT, "User with id $id has no notifications"
        )
        return notifications
    }

    @PutMapping("/users/{id}")
    fun updateNotificationToRead(
        @PathVariable id: Long,
        @RequestBody notificationDto: NotificationDto,
        @AuthenticationPrincipal principal: UserDetailsImpl
    ): NotificationDto? {
        logger.debug("[x] Received request with principal: {}", principal)
        validateNotificationUpdate(notificationDto, principal.id)
        return notificationService.updateNotificationToRead(notificationDto)
    }

    private fun validateNotificationUpdate(notificationDto: NotificationDto, userId: Long) {
        if (notificationDto.user?.id == userId)
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Notification does not belong to current user"
            )
    }
}