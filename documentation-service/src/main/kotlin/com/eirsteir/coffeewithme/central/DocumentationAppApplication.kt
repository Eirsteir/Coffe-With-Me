package com.eirsteir.coffeewithme.central

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import springfox.documentation.swagger2.annotations.EnableSwagger2

@EnableSwagger2
@EnableScheduling
@SpringBootApplication
class DocumentationAppApplication

fun main(args: Array<String>) {
    runApplication<DocumentationAppApplication>(*args)
}