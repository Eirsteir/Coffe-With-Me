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
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;


@ExtendWith(SpringExtension.class)
@Import({RedisTestConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

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
    void testGetNotificationsWhenNotifications_thenReturnNotifications() {
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