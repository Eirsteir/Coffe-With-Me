package com.eirsteir.coffeewithme.notification

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.EnableEurekaClient

@EnableEurekaClient
@SpringBootApplication
class NotificationServiceApplication


fun main(args: Array<String>) {
    runApplication<NotificationServiceApplication>(*args)
}
