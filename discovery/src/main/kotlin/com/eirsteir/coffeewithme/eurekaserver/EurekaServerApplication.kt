package com.eirsteir.coffeewithme.eurekaserver

import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@EnableEurekaServer
@SpringBootApplication
class EurekaServerApplication

fun main(args: Array<String>) {
    runApplication<EurekaServerApplication>(*args)
}
