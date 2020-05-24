package com.eirsteir.coffeewithme.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@ComponentScan(basePackages = {"com.eirsteir.coffeewithme.service.user"} )
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  private final String BASE_URL = "/api";

  @Autowired
  private JwtConfig jwtConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
          http
                  .csrf().disable()
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                      .exceptionHandling().authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                  .and()
                      .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                      .authorizeRequests()
                      .antMatchers(BASE_URL).permitAll()
                      .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                      .antMatchers(BASE_URL + "/actuator/**").hasRole("ADMIN")
                      .antMatchers(BASE_URL + "/swagger-ui").permitAll()
                      .anyRequest().authenticated();
  }

  @Override
  public void configure(WebSecurity web) {
    web.ignoring().antMatchers(
      "/resources/**", "/static/**", "/css/**", "/js/**", "/images/**",
      "/resources/static/**", "/css/**", "/js/**", "/img/**", "/fonts/**",
      "/images/**", "/scss/**", "/vendor/**", "/favicon.ico", "/auth/**", "/favicon.png",
      "/v2/api-docs", "/configuration/ui", "/configuration/security", "/swagger-ui.html",
      "/webjars/**", "/swagger-resources/**", "/api/swagge‌​r-ui.html/**", "/actuator",
      "/actuator/**", "/*.bundle.*");
  }

  @Bean
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }

}
