package com.eirsteir.coffeewithme.social;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;

@EnableEurekaClient
@ComponentScan(
        basePackages = {
                "com.eirsteir.coffeewithme.commons.exception",
                "com.eirsteir.coffeewithme.social"
        })
@SpringBootApplication
public class SocialServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(SocialServiceApplication.class, args);
  }
}
