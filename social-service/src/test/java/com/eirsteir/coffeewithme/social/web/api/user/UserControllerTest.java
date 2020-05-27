package com.eirsteir.coffeewithme.social.web.api.user;

import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import com.eirsteir.coffeewithme.social.web.api.friendship.FriendshipController;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Import({SecurityConfig.class})
@TestPropertySource("classpath:exception.properties")
@WebMvcTest(FriendshipController.class)
@ExtendWith(SpringExtension.class)
class UserControllerTest {


}