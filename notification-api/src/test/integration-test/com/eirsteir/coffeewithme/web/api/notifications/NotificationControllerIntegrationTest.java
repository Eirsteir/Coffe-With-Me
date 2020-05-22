package com.eirsteir.coffeewithme.web.api.notifications;


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
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Import(ModelMapperConfig.class)
@SpringBootTest(classes = NotificationApiApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";

    private Notification newestNotification;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
    }

    @Test
    void testGetNotificationsWhenNotifications_thenReturnNotifications() throws Exception {

        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetNotificationsOnePerPageWhenNotifications_thenReturnNewestNotification() throws Exception {

        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(newestNotification.getId().intValue())))
                .andExpect(jsonPath("$[0].seen", is(false)))
                .andExpect(jsonPath("$[0].requestedByViewer", is(false)));
    }

    @Test
    void testGetNotificationsWhenNoNotifications_thenReturnHttp204() throws Exception {
        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetNotificationsWhenUnauthorized_thenReturnHttp401() throws Exception {
        mvc.perform(get("/notifications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testUpdateNotificationToReadWhenFound_thenReturnUpdatedNotification() throws Exception {
        NotificationDto notificationDtoToUpdate = modelMapper.map(newestNotification, NotificationDto.class);

        mvc.perform(put("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seen", is(true)));
    }

//    @Test
//    void testUpdateNotificationToReadWhenNotFound_thenReturnHttp400() throws Exception {
//
//
//        mvc.perform(put("/notifications")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(JSONUtils.asJsonString(notificationDtoNotFound)))
//                .andExpect(status().isNotFound())
//                .andExpect(jsonPath("$.message",
//                                    equalTo("Requested notification with id - " +
//                                                    notificationDtoNotFound.getId() + " does not exist")));
//    }

    @Test
    void testUpdateNotificationToReadWhenNotToCurrentUser_thenReturnHttp400() throws Exception {
        NotificationDto notificationDtoToUpdate = modelMapper.map(newestNotification, NotificationDto.class);

        mvc.perform(put("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoToUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message",
                                    equalTo("Notification does not belong to current user")));
    }
}