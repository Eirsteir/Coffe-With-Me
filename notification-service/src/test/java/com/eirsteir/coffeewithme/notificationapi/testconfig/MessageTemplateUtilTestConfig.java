package com.eirsteir.coffeewithme.notificationapi.testconfig;

import com.eirsteir.coffeewithme.notificationapi.config.PropertiesConfig;
import com.eirsteir.coffeewithme.notificationapi.exception.MessageTemplateUtil;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MessageTemplateUtilTestConfig {

    @Bean
    public MessageTemplateUtil messageTemplateUtil(){
        return new MessageTemplateUtil(propertiesConfig());
    }

    @Bean
    public PropertiesConfig propertiesConfig() {
        return new PropertiesConfig();
    }

}
