package com.eirsteir.coffeewithme.social.web.api.user

import com.eirsteir.coffeewithme.config.EventuateTestConfig
import org.springframework.context.annotation.Import

@ActiveProfiles("test")
@Import(SecurityConfig::class, ModelMapperConfig::class, EventuateTestConfig::class)
@WebMvcTest(
    UserController::class
)
internal class UserControllerTest 