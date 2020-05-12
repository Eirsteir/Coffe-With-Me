package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.security.SecurityConfig;
import config.CWMExceptionTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({SecurityConfig.class, CWMExceptionTestConfig.class})
@SpringBootTest
class CoffeeWithMeApplicationTests {

    @Test
    void contextLoads() {
    }

}
