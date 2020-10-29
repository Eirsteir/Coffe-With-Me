package com.eirsteir.coffeewithme.social.service.coffeebreak;

import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.config.SetupTestDataLoader;
import com.eirsteir.coffeewithme.social.SocialServiceApplication;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import({ModelMapperConfig.class, SetupTestDataLoader.class})
@SpringBootTest(classes = {SocialServiceApplication.class, SecurityConfig.class, EventuateTestConfig.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CoffeeBreakServiceImplIntegrationTest {

}