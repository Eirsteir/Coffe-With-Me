package com.eirsteir.coffeewithme.commons.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:exception.properties")
public class PropertiesConfig {

  @Autowired private Environment env;

  public String getConfigValue(String configKey) {
    return env.getProperty(configKey);
  }
}
