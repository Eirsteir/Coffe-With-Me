package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.domain.notification.Notification;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.dto.NotificationDto;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.repository.UserRepository;
import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import com.eirsteir.coffeewithme.testconfig.SetupTestDataLoader;
import com.eirsteir.coffeewithme.util.JSONUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Import({RedisTestConfig.class, SetupTestDataLoader.class, ModelMapperConfig.class})
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

    private static final String ADDRESSEE_EMAIL = "addressee@test.com";
    public static final String OTHER_USER_EMAIL = "other-user@test.com";

    private Notification newestNotification;
    private User addressee;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Autowired
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        newestNotification = notificationRepository.findById(7L).get();
        addressee = userRepository.findByEmail(ADDRESSEE_EMAIL).get();

        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetNotificationsWhenNotifications_thenReturnNotifications() throws Exception {

        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "2")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetNotificationsOnePerPageWhenNotifications_thenReturnNewestNotification() throws Exception {

        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", equalTo(newestNotification.getId().intValue())));
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testGetNotificationsWhenNoNotifications_thenReturnHttp204() throws Exception {
        mvc.perform(get("/notifications")
                            .param("page", "0")
                            .param("size", "1")
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andExpect(jsonPath("$.message", equalTo("User with email " +
                                                                 OTHER_USER_EMAIL +
                                                                 " has no notifications")));
    }

    @Test
    void testGetNotificationsWhenUnauthorized_thenReturnHttp401() throws Exception {
        mvc.perform(get("/notifications"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testUpdateNotificationToReadWhenFound_thenReturnUpdatedNotification() throws Exception {
        NotificationDto notificationDtoToUpdate = modelMapper.map(newestNotification, NotificationDto.class);

        mvc.perform(put("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isRead", is(true)));
    }

    @Test
    @WithUserDetails(value = ADDRESSEE_EMAIL, userDetailsServiceBeanName = "userDetailsService")
    void testUpdateNotificationToReadWhenNotFound_thenReturnHttp400() throws Exception {
        NotificationDto notificationDtoNotFound = NotificationDto.builder()
                .id(100L)
                .toUserId(addressee.getId())
                .build();

        mvc.perform(put("/notifications")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(JSONUtils.asJsonString(notificationDtoNotFound)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message",
                                    equalTo("Requested notification with id - " +
                                                    notificationDtoNotFound.getId() + " does not exist")));
    }

    @Test
    @WithUserDetails(value = OTHER_USER_EMAIL, userDetailsServiceBeanName = "userDetailsService")
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