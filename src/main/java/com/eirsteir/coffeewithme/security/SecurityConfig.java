package com.eirsteir.coffeewithme.security;

import com.eirsteir.coffeewithme.service.CMEUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.WebApplicationContext;

import javax.annotation.PostConstruct;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private WebApplicationContext applicationContext;

  @Autowired
  private CMEUserDetailsService userDetailsService;

  @Autowired
  private AuthenticationSuccessHandlerImpl successHandler;


  @PostConstruct
  public void completeSetup() {
    userDetailsService = applicationContext.getBean(CMEUserDetailsService.class);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) {
          auth
            .authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
          http
              .httpBasic().and()
              .logout().and()
              .authorizeRequests()
                  .antMatchers( "/api", "/error", "/signup").permitAll()
                  .anyRequest().authenticated()
              .and()
                  .sessionManagement()
                  .sessionCreationPolicy(ALWAYS)
              .and()
              .csrf().disable();
//                  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
      "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
      "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
      "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
      "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
      "/webjars/**", "/swagger-resources/**", "/swagge‌​r-ui.html", "/actuator",
      "/actuator/**", "/*.bundle.*");
  }


  @Bean
  public BCryptPasswordEncoder encoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(encoder());
    return authProvider;
  }


}
