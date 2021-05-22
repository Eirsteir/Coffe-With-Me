package com.eirsteir.coffeewithme.central.config.swagger

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider
import springfox.documentation.swagger.web.SwaggerResource
import springfox.documentation.swagger.web.SwaggerResourcesProvider
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.util.*

/**
 * Swagger Ui configurations. Configure bean of the [SwaggerResourcesProvider] to read data
 * from in-memory context
 */
@Configuration
@EnableSwagger2
class SwaggerUIConfiguration(private val definitionContext: ServiceDefinitionsContext) {

    @Bean
    fun configureTemplate() = RestTemplate()

    @Primary
    @Bean
    @Lazy
    fun swaggerResourcesProvider(
        defaultResourcesProvider: InMemorySwaggerResourcesProvider, temp: RestTemplate
    ) = SwaggerResourcesProvider {
            val resources: MutableList<SwaggerResource?> = ArrayList(defaultResourcesProvider.get())
            resources.clear()
            resources.addAll(definitionContext.getSwaggerDefinitions())
            resources
        }
}