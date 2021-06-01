package com.eirsteir.coffeewithme.notification.service

import com.eirsteir.coffeewithme.commons.domain.notification.NotificationType
import com.eirsteir.coffeewithme.commons.exception.APIException
import com.eirsteir.coffeewithme.commons.test.BaseUnitTest
import com.eirsteir.coffeewithme.notification.config.ModelMapperConfig
import com.eirsteir.coffeewithme.notification.domain.Notification
import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import org.assertj.core.api.Assertions
import org.assertj.core.api.ThrowableAssert.ThrowingCallable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ActiveProfiles("test")
@ExtendWith(MockitoExtension::class)
internal class NotificationServiceImplTest  {

    private val TO_USER_ID = 1L
    private val CURRENT_USER_ID = 2L
    private lateinit var friendRequestNotification: Notification
    private lateinit var currentUser: Notification.UserDetails

    @InjectMocks
    private lateinit var service: NotificationService

    @Mock
    private lateinit var notificationRepository: NotificationRepository

    private val modelMapper: ModelMapper = ModelMapper()

    @BeforeEach
    fun setUp() {
        currentUser = Notification.UserDetails(id = CURRENT_USER_ID, name = "From user")
        friendRequestNotification = Notification(
            notificationId = 1L,
            subjectId = TO_USER_ID,
            type = NotificationType.FRIEND_REQUEST,
            user = currentUser
        )
    }

    @Test
    fun testFindAllByUserWhenUserHasNotifications_thenReturnListOfNotificationDto() {
        val firstPage: Pageable = PageRequest.of(0, 2)
        Mockito.`when`(notificationRepository.findAllByUser_idOrderByTimestamp(TO_USER_ID, firstPage))
            .thenReturn(
                listOf(
                    Notification(user = currentUser),
                    Notification(user = currentUser)
                )
            )
        val notifications = service.findAllByUserId(TO_USER_ID, firstPage)

        Assertions.assertThat(notifications).hasSize(2)
        Assertions.assertThat(notifications[0].user!!.id).isEqualTo(currentUser.id)
    }

    @Test
    fun testUpdateNotificationToReadWhenFound_thenSetIsReadToTrue() {
        friendRequestNotification.seen = true
        val notification = friendRequestNotification.copy(
            seen = false
        )

        Mockito.`when`(notificationRepository.findById(friendRequestNotification.notificationId!!))
            .thenReturn(Optional.ofNullable(notification))

        Mockito.`when`(
            notificationRepository.save(
                Mockito.any(
                    Notification::class.java
                )
            )
        )
            .thenReturn(friendRequestNotification)

        val notificationDtoToUpdate = modelMapper.map(notification, NotificationDto::class.java)
        val updatedNotificationDto = service.updateNotificationToRead(notificationDtoToUpdate)

        Assertions.assertThat(updatedNotificationDto.seen).isTrue
    }

    @Test
    fun testUpdateNotificationToReadWhenNotFound_thenThrowEntityNotFoundException() {
        Mockito.`when`(notificationRepository.findById(friendRequestNotification.notificationId!!))
            .thenReturn(Optional.empty())

        val notificationDtoToUpdate = modelMapper.map(friendRequestNotification, NotificationDto::class.java)

        Assertions.assertThatExceptionOfType(APIException.EntityNotFoundException::class.java)
            .isThrownBy(ThrowingCallable { service.updateNotificationToRead(notificationDtoToUpdate) })
    }
}