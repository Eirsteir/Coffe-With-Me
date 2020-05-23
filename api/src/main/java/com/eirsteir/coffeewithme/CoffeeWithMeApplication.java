package com.eirsteir.coffeewithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableZuulProxy
@EnableRedisHttpSession(flushMode = FlushMode.IMMEDIATE)
@SpringBootApplication
public class CoffeeWithMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeWithMeApplication.class, args);
    }

}
