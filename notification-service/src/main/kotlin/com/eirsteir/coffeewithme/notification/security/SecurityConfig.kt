package com.eirsteir.coffeewithme.notification.security

import com.eirsteir.coffeewithme.commons.security.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
@EnableWebSecurity
@ComponentScan(basePackages = ["com.eirsteir.coffeewithme.commons"])
class SecurityConfig(private val jwtConfig: JwtConfig) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint { _: HttpServletRequest, res: HttpServletResponse, _: AuthenticationException ->
                res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED
                )
            }
            .and()
            .addFilterAfter(
                JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter::class.java
            )
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, "/notifications")
            .permitAll() // TODO: 25.05.2020 remove this
            .anyRequest()
            .authenticated()
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