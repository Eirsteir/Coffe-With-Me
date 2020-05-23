package com.eirsteir.coffeewithme.notificationapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableRedisHttpSession(flushMode = FlushMode.IMMEDIATE)
@SpringBootApplication
public class NotificationApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApiApplication.class, args);
    }

}
