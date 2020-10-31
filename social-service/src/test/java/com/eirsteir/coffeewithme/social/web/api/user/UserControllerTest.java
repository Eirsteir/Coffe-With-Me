package com.eirsteir.coffeewithme.social.web.api.user;

import com.eirsteir.coffeewithme.config.EventuateTestConfig;
import com.eirsteir.coffeewithme.social.config.ModelMapperConfig;
import com.eirsteir.coffeewithme.social.security.SecurityConfig;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@Import({SecurityConfig.class, ModelMapperConfig.class, EventuateTestConfig.class})
@WebMvcTest(UserController.class)
class UserControllerTest {}
