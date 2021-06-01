package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.commons.exception.APIException
import com.eirsteir.coffeewithme.commons.exception.EntityType
import com.eirsteir.coffeewithme.commons.exception.ExceptionType
import com.eirsteir.coffeewithme.notification.domain.Notification
import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import mu.KotlinLogging
import org.modelmapper.ModelMapper
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.stream.Collectors

private val logger = KotlinLogging.logger {  }

@Service
class NotificationServiceImpl(
    private val notificationRepository: NotificationRepository,
    private val modelMapper: ModelMapper
) : NotificationService {

    override fun updateNotificationToRead(notificationDto: NotificationDto): NotificationDto =
        findById(notificationDto.notificationId)
            .run {
                this.seen = true

                logger.debug("[x] Updating notification: {}", this)

                return modelMapper.map(
                    notificationRepository.save(this), NotificationDto::class.java
                )
            }

    private fun findById(id: Long): Notification =
        notificationRepository.findById(id)
            .orElseThrow {
                throw APIException.of(
                    EntityType.NOTIFICATION,
                    ExceptionType.ENTITY_NOT_FOUND,
                    id.toString()
                )
            }

    override fun findAllByUserId(id: Long, pageable: Pageable): MutableList<NotificationDto> {
        return notificationRepository.findAllByUser_idOrderByTimestamp(id, pageable)
            .stream()
            .map { notification: Notification -> modelMapper.map(notification, NotificationDto::class.java) }
            .collect(Collectors.toList())
    }
}