package com.eirsteir.coffeewithme.api.web.api.user;

import com.eirsteir.coffeewithme.api.security.SecurityConfig;
import com.eirsteir.coffeewithme.testconfig.MessageTemplateUtilTestConfig;
import com.eirsteir.coffeewithme.api.web.api.friendship.FriendshipController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({SecurityConfig.class, MessageTemplateUtilTestConfig.class})
@TestPropertySource("classpath:exception.properties")
@WebMvcTest(FriendshipController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {


}