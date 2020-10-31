package com.eirsteir.coffeewithme.notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableEurekaClient
@EnableJpaRepositories(basePackages = "com.eirsteir.coffeewithme.notification.repository")
@ComponentScan(
    basePackages = {
      "com.eirsteir.coffeewithme.commons.exception",
      "com.eirsteir.coffeewithme.notification"
    })
@SpringBootApplication
public class NotificationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }
}
