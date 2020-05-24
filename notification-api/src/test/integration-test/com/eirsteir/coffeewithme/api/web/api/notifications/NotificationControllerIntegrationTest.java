package com.eirsteir.coffeewithme.api.web.api.notifications;


import com.eirsteir.coffeewithme.SetupTestDataLoader;
import com.eirsteir.coffeewithme.notificationapi.NotificationApiApplication;
import com.eirsteir.coffeewithme.notificationapi.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.notificationapi.domain.Notification;
import com.eirsteir.coffeewithme.notificationapi.dto.NotificationDto;
import com.eirsteir.coffeewithme.notificationapi.repository.NotificationRepository;
import com.eirsteir.coffeewithme.notificationapi.testutil.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Import({ModelMapperConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = NotificationApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

    public static final long DEFAULT_USER_ID = 1L;
    public static final long REQUESTER_ID = 2L;
    public static final long ADDRESSEE_ID = 3L;
    public static final long OTHER_USER_ID = 4L;

    private Notification mostResentNotificationToRequester;

    @Autowired
    private NotificationRepository repository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();

        mostResentNotificationToRequester = repository.
                findAllByUser_idOrderByTimestamp(REQUESTER_ID, PageRequest.of(0, 1)).get(0);
    }

    @Test
    void testGetNotificationsWhenNotifications_thenReturnNotifications() throws Exception {

        mvc.perform(get("/notifications/users/{id}", REQUESTER_ID)
                            .param("page", "0")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetNotificationsOnePerPageWhenNotifications_thenReturnNewestNotification() throws Exception {

        mvc.perform(get("/notifications/users/{id}", REQUESTER_ID)
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].notificationId", equalTo(mostResentNotificationToRequester.getNotificationId().intValue())))
                .andExpect(jsonPath("$[0].seen", is(false)));
    }

    @Test
    void testGetNotificationsWhenNoNotifications_thenReturnHttp204() throws Exception {

        mvc.perform(get("/notifications/users/{id}", ADDRESSEE_ID)
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateNotificationToReadWhenFound_thenReturnUpdatedNotification() throws Exception {
        NotificationDto notificationDtoToUpdate = modelMapper.map(mostResentNotificationToRequester, NotificationDto.class);

        mvc.perform(put("/notifications/users/{id}", REQUESTER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seen", is(true)));
    }

    @Test
    void testUpdateNotificationToReadWhenNotFound_thenReturnHttp400() throws Exception {
        NotificationDto notificationDtoNotFound = NotificationDto.builder()
                .notificationId(100L)
                .build();

        mvc.perform(put("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoNotFound)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                                    equalTo("Requested notification with id - " +
                                                    notificationDtoNotFound.getNotificationId() + " does not exist")));
    }

    @Test
    void testUpdateNotificationToReadWhenNotToCurrentUser_thenReturnHttp400() throws Exception {
        NotificationDto notificationDtoToUpdate = modelMapper.map(mostResentNotificationToRequester, NotificationDto.class);

        mvc.perform(put("/notifications/users/{id}", REQUESTER_ID)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoToUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                                    equalTo("Notification does not belong to current user")));
    }
}