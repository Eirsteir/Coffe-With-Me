package com.eirsteir.coffeewithme;

import org.springframework.boot.SpringApplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.security.config.http.SessionCreationPolicy.NEVER;

@SpringBootApplication
@RestController
public class CoffeeWithMeApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(CoffeeWithMeApplication.class, args);
    }

    // TODO: 05.05.2020 should not only be writers?
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // We need this to prevent the browser from popping up a dialog on a 401
        http.httpBasic().disable().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        http.authorizeRequests().antMatchers(HttpMethod.POST, "/**").hasRole("WRITER")
                .anyRequest().authenticated();
        http.sessionManagement().sessionCreationPolicy(NEVER);
    }

}
