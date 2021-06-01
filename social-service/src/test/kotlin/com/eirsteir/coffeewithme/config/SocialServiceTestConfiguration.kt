package com.eirsteir.coffeewithme.config

import com.eirsteir.coffeewithme.social.config.SocialServiceConfiguration
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import

@Configuration
@EnableAutoConfiguration
@Import(
    SocialServiceConfiguration::class, TramConsumerCommonConfiguration::class
)
class SocialServiceTestConfiguration 