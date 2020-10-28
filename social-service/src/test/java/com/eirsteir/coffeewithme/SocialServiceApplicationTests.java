package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.social.SocialServiceApplication;
import com.eirsteir.coffeewithme.testconfig.EventuateTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(classes = {SocialServiceApplication.class, EventuateTestConfig.class})
class SocialServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}


