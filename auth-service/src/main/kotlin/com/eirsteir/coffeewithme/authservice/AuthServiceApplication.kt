package com.eirsteir.coffeewithme.authservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.context.annotation.ComponentScan

@EnableEurekaClient
@ComponentScan(basePackages = ["com.eirsteir.coffeewithme.commons.exception", "com.eirsteir.coffeewithme.authservice"])
@SpringBootApplication
class AuthServiceApplication

fun main(args: Array<String>) {
    runApplication<AuthServiceApplication>(*args)
}