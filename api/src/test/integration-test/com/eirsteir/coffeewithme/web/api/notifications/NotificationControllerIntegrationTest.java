package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.repository.NotificationRepository;
import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import com.eirsteir.coffeewithme.testconfig.SetupTestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@Import({RedisTestConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

    private static final String ADDRESSEE_EMAIL = "addressee@test.com";

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @BeforeEach
    void setUp() {
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
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void testGetNotificationsWhenNoNotifications_thenReturnHttp204() {
    }

    @Test
    void testGetNotificationsWhenUnauthorized_thenReturnHttp401() {
    }

    @Test
    void testUpdateNotificationWhenFound_thenReturnUpdatedNotification() {
    }
}