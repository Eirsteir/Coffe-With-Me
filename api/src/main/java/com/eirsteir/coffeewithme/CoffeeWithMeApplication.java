package com.eirsteir.coffeewithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@EnableZuulProxy
@SpringBootApplication
public class CoffeeWithMeApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeWithMeApplication.class, args);
    }

}
