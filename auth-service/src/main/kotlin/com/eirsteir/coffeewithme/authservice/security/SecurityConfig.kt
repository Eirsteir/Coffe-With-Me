package com.eirsteir.coffeewithme.authservice.security

import com.eirsteir.coffeewithme.authservice.repository.AccountRepository
import com.eirsteir.coffeewithme.authservice.service.UserDetailsServiceImpl
import com.eirsteir.coffeewithme.commons.security.JwtConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@EnableWebSecurity
@PropertySource("classpath:env.properties")
class SecurityConfig(
    val jwtconfig: JwtConfig,
    private val accountRepository: AccountRepository
) : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http.cors()
            .and()
            .csrf()
            .disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .exceptionHandling()
            .authenticationEntryPoint { req: HttpServletRequest?, res: HttpServletResponse, e: AuthenticationException ->
                res.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    e.message
                )
            }
            .and()
            .addFilter(
                JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtconfig)
            )
            .authorizeRequests()
            .antMatchers(HttpMethod.POST, jwtconfig.uri + "/**")
            .permitAll()
            .anyRequest()
            .authenticated()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder())
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

    @Bean
    override fun userDetailsService(): UserDetailsService {
        return UserDetailsServiceImpl(accountRepository)
    }

    @Bean
    fun jwtConfig(): JwtConfig {
        return JwtConfig()
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}