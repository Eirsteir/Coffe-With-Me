package com.eirsteir.coffeewithme.testconfig;

import com.eirsteir.coffeewithme.config.PropertiesConfig;
import com.eirsteir.coffeewithme.util.MessageTemplateUtil;
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
