package com.eirsteir.coffeewithme.commons.exception

import org.springframework.context.annotation.PropertySource
import org.springframework.core.env.Environment
import org.springframework.stereotype.Component

@Component
@PropertySource("classpath:exception.properties")
class PropertiesConfig(private val env: Environment) {

    fun getConfigValue(configKey: String): String? = env.getProperty(configKey)

}