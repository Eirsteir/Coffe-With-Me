package com.eirsteir.coffeewithme

import com.eirsteir.coffeewithme.config.EventuateTestConfig
import com.eirsteir.coffeewithme.social.SocialServiceApplication
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@ActiveProfiles("test")
@SpringBootTest(classes = [SocialServiceApplication::class, EventuateTestConfig::class])
internal class SocialServiceApplicationTests {
    @Test
    fun contextLoads() {
    }
}