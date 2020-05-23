package com.eirsteir.coffeewithme.notificationapi.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER)
                .and()
                    .authorizeRequests()
                    .antMatchers( "/actuator/**").permitAll() // .hasRole("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable();
    }
}