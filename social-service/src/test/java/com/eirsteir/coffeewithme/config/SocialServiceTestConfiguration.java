package com.eirsteir.coffeewithme.config;


import com.eirsteir.coffeewithme.social.config.SocialServiceConfiguration;
import io.eventuate.tram.spring.consumer.common.TramConsumerCommonConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({SocialServiceConfiguration.class,
        TramConsumerCommonConfiguration.class})
public class SocialServiceTestConfiguration {
}
