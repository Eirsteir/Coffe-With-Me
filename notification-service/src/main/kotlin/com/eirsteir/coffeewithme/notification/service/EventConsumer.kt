package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.commons.domain.notification.AbstractEntityNotificationEvent
import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType
import com.eirsteir.coffeewithme.notification.domain.Notification
import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.simp.SimpMessagingTemplate

private val logger = KotlinLogging.logger { }

abstract class EventConsumer(
    private val notificationRepository: NotificationRepository,
    private val template: SimpMessagingTemplate,
    private val modelMapper: ModelMapper
) {

    protected fun handleEvent(domainEvent: AbstractEntityNotificationEvent) {
        val notification = createNotification(domainEvent)
        notificationRepository.save(notification)

        logger.info("[x] Registered notification: {}", notification)

        convertAndSendToUser(notification)
    }

    private fun createNotification(friendshipEvent: AbstractEntityNotificationEvent): Notification {
        val subjectId = friendshipEvent.subjectId
        val userDetails = modelMapper.map(friendshipEvent.user, Notification.UserDetails::class.java)

        return Notification(
            subjectId = subjectId,
            user = userDetails,
            type = NotificationType.FRIEND_REQUEST
        )
    }

    private fun convertAndSendToUser(notification: Notification) {
        logger.info("[x] Sending notification: {}", notification)

        val notificationDto = modelMapper.map(notification, NotificationDto::class.java)

        template.convertAndSendToUser(
            notification.subjectId.toString(), "/queue/notifications", notificationDto
        )
    }
}