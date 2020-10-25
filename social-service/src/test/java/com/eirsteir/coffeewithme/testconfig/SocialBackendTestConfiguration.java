package com.eirsteir.coffeewithme.testconfig;


import com.eirsteir.coffeewithme.social.config.SocialBackendConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({SocialBackendConfiguration.class})
public class SocialBackendTestConfiguration {
}
