package com.eirsteir.coffeewithme.notification

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType
import com.eirsteir.coffeewithme.notification.domain.Notification
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import mu.KotlinLogging
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

private val logger = KotlinLogging.logger {  }

@Component
class SetupTestDataLoader(
    private val repository: NotificationRepository,
    private var alreadySetup: Boolean = false
) : ApplicationListener<ContextRefreshedEvent?> {

    @Transactional
    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        if (alreadySetup) {
            return
        }
        val requester = Notification.UserDetails(
            id = REQUESTER_ID,
            name = "Requester",
            nickname = REQUESTER_NICKNAME
            )
        val addressee = Notification.UserDetails(
            id = ADDRESSEE_ID,
            name = "Addressee",
            nickname = ADDRESSEE_NICKNAME
            )
        val otherUser = Notification.UserDetails(
            id = OTHER_USER_ID,
            name = "Other User",
            nickname = OTHER_USER_NICKNAME
            )
        var notification = Notification(
            subjectId = DEFAULT_USER_ID,
            type = NotificationType.FRIENDSHIP_ACCEPTED,
            user = addressee
            )

        logger.debug("[x] Preloading {}", repository.save(notification))
        notification = Notification(
            subjectId = REQUESTER_ID,
            type = NotificationType.FRIENDSHIP_ACCEPTED,
            user = addressee
            )

        logger.debug("[x] Preloading {}", repository.save(notification))
        notification = Notification(
            subjectId = OTHER_USER_ID,
            type = NotificationType.FRIEND_REQUEST,
            user = requester
            )

        logger.debug("[x] Preloading {}", repository.save(notification))
        alreadySetup = true
    }

    companion object {
        const val DEFAULT_USER_ID = 1L
        const val REQUESTER_ID = 2L
        const val ADDRESSEE_ID = 3L
        const val OTHER_USER_ID = 4L
        const val REQUESTER_NICKNAME: String = "requester"
        const val ADDRESSEE_NICKNAME: String = "addressee"
        const val OTHER_USER_NICKNAME: String = "other-user"
    }
}