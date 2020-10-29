package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.social.SocialServiceApplication;
import com.eirsteir.coffeewithme.config.SetupTestDataLoader;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@Import({SetupTestDataLoader.class})
@SpringBootTest(classes = SocialServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoffeeBreakServiceImplIntegrationTest {

}