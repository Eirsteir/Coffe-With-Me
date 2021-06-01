package com.eirsteir.coffeewithme.social

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class SocialServiceApplication

fun main(args: Array<String>) {
    runApplication<SocialServiceApplication>( *args)
}
