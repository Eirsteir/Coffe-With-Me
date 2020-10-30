package com.eirsteir.coffeewithme.notification;

import com.eirsteir.coffeewithme.notification.config.EventuateTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {NotificationServiceApplication.class, EventuateTestConfig.class})
class NotificationServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
