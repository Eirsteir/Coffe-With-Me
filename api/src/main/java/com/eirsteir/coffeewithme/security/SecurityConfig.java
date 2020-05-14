package com.eirsteir.coffeewithme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

import static org.springframework.security.config.http.SessionCreationPolicy.ALWAYS;

@Configuration
@ComponentScan(basePackages = {"com.eirsteir.coffeewithme.service.user"} )
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String BASE_URL = "/api";

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private AuthenticationSuccessHandlerImpl successHandler;

  @Override
  public void configure(AuthenticationManagerBuilder auth) {
          auth.authenticationProvider(authenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
          http
              .httpBasic().and()
              .logout().and()
              .authorizeRequests()
                  .antMatchers( BASE_URL).permitAll()
                  .antMatchers("/error").permitAll()
                  .antMatchers(BASE_URL + "/user/registration").permitAll()
                  .antMatchers(BASE_URL + "/swagger-ui").permitAll()
                  .antMatchers("/h2-console/**").permitAll()
                  .anyRequest().authenticated()
              .and()
                  .logout()
                  .logoutUrl(BASE_URL + "/logout")
              .and()
                  .sessionManagement()
                  .sessionCreationPolicy(ALWAYS)
              .and()
                  .exceptionHandling()
                  .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
              .and()
              .csrf().disable()
              .headers().frameOptions().disable(); // Allow H2-console

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
  public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    final DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

}
