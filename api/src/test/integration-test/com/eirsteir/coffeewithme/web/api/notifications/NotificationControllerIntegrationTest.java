package com.eirsteir.coffeewithme.web.api.notifications;

import com.eirsteir.coffeewithme.CoffeeWithMeApplication;
import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import com.eirsteir.coffeewithme.testconfig.SetupTestDataLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@Import({RedisTestConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = CoffeeWithMeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NotificationControllerIntegrationTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void getNotifications() {
    }

    @Test
    void updateNotification() {
    }
}