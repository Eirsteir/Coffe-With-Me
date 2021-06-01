package com.eirsteir.coffeewithme.central.web

import com.eirsteir.coffeewithme.central.config.swagger.ServiceDefinitionsContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
class ServiceDefinitionController(private val definitionContext: ServiceDefinitionsContext) {

    @GetMapping("/service/{servicename}")
    fun getServiceDefinition(@PathVariable("servicename") serviceName: String) =
        definitionContext.getSwaggerDefinition(serviceName)

}