package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.testconfig.RedisTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(RedisTestConfig.class)
@SpringBootTest
class CoffeeWithMeApplicationTests {

    @Test
    void contextLoads() {
    }

}
