package com.eirsteir.coffeewithme.notification.web.api

import com.eirsteir.coffeewithme.notification.domain.Notification
import com.eirsteir.coffeewithme.notification.dto.NotificationDto
import com.eirsteir.coffeewithme.notification.repository.NotificationRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@AutoConfigureMockMvc
@SpringBootTest
internal class NotificationControllerIntegrationTest {

    private lateinit var mostResentNotificationToRequester: Notification

    @Autowired
    private lateinit var repository: NotificationRepository

    @Autowired
    private lateinit var modelMapper: ModelMapper

    @Autowired
    private lateinit var context: WebApplicationContext

    @Autowired
    private lateinit var mvc: MockMvc

    private val objectMapper: ObjectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build()
        mostResentNotificationToRequester = repository.findAllByUser_idOrderByTimestamp(
            REQUESTER_ID,
            PageRequest.of(0, 1)
        )[0]
    }

    @Test
    fun testGetNotificationsWhenNotifications_thenReturnNotifications() {
        mvc.perform(
            MockMvcRequestBuilders.get("/notifications/users/{id}", REQUESTER_ID)
                .param("page", "0")
                .param("size", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any?>(1)))
    }

    @Test
    fun testGetNotificationsOnePerPageWhenNotifications_thenReturnNewestNotification() {
        mvc.perform(
            MockMvcRequestBuilders.get("/notifications/users/{id}", REQUESTER_ID)
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any?>(1)))
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "$[0].notificationId",
                    Matchers.equalTo(mostResentNotificationToRequester.notificationId?.toInt())
                )
            )
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].seen", Matchers.`is`(false)))
    }

    @Test
    fun testGetNotificationsWhenNoNotifications_thenReturnHttp204() {
        mvc.perform(
            MockMvcRequestBuilders.get("/notifications/users/{id}", ADDRESSEE_ID)
                .param("page", "0")
                .param("size", "1")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun testUpdateNotificationToReadWhenFound_thenReturnUpdatedNotification() {
        val notificationDtoToUpdate = modelMapper.map(mostResentNotificationToRequester, NotificationDto::class.java)
        mvc.perform(
            MockMvcRequestBuilders.put("/notifications/users/{id}", REQUESTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDtoToUpdate))
        )
            .andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.seen", Matchers.`is`(true)))
    }

    @Test
    fun testUpdateNotificationToReadWhenNotFound_thenReturnHttp400() {
        val notificationDtoNotFound = NotificationDto(notificationId = 100L)
        mvc.perform(
            MockMvcRequestBuilders.put("/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDtoNotFound))
        )
            .andExpect(MockMvcResultMatchers.status().isNotFound)
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "$.message",
                    Matchers.equalTo(
                        "Requested notification with id - "
                                + notificationDtoNotFound.notificationId
                                + " does not exist"
                    )
                )
            )
    }

    @Test
    fun testUpdateNotificationToReadWhenNotToCurrentUser_thenReturnHttp400() {
        val notificationDtoToUpdate = modelMapper.map(mostResentNotificationToRequester, NotificationDto::class.java)
        mvc.perform(
            MockMvcRequestBuilders.put("/notifications/users/{id}", REQUESTER_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(notificationDtoToUpdate))
        )
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
            .andExpect(
                MockMvcResultMatchers.jsonPath(
                    "$.message",
                    Matchers.equalTo("Notification does not belong to current user")
                )
            )
    }

    companion object {
        const val REQUESTER_ID = 2L
        const val ADDRESSEE_ID = 3L
    }
}