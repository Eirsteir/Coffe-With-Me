package com.eirsteir.coffeewithme.notification

import com.eirsteir.coffeewithme.notification.config.EventuateTestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [NotificationServiceApplication::class, EventuateTestConfig::class])
internal class NotificationServiceApplicationTests {
    @Test
    fun contextLoads() {
    }
}