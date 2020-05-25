package com.eirsteir.coffeewithme;

import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.testconfig.SetupTestDataLoader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SetupTestDataLoader.class, ModelMapperConfig.class})
@SpringBootTest
class SocialServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
