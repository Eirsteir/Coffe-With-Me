package com.eirsteir.coffeewithme.gateway.security

import com.eirsteir.coffeewithme.commons.security.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@PropertySource("classpath:env.properties")
@ComponentScan(basePackages = ["com.eirsteir.coffeewithme.commons"])
class SecurityConfig(private val jwtConfig: JwtConfig) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint { _: HttpServletRequest, res: HttpServletResponse, e: AuthenticationException ->
                res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
                )
            }
            .and()
            .addFilterAfter(
                JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, jwtConfig.Uri + "/**")
            .permitAll()
            .antMatchers("/actuator/**")
            .hasRole("ADMIN")
            .antMatchers("/docs/**")
            .permitAll()
            .anyRequest()
            .authenticated()
    }

    @Bean
    fun corsFilter(): CorsFilter {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.allowCredentials = true
        config.addAllowedOrigin("*") // TODO: lock down before deploying
        config.addAllowedHeader("*")
        config.addExposedHeader(HttpHeaders.AUTHORIZATION)
        config.addAllowedMethod("*")
        source.registerCorsConfiguration("/**", config)
        return CorsFilter(source)
    }

    override fun configure(web: WebSecurity) {
        web.ignoring()
            .antMatchers(
                "/v2/api-docs",
                "/configuration/ui",
                "/swagger-resources/**",
                "/configuration/security",
                "/swagger-ui.html",
                "/webjars/**"
            )
    }
}