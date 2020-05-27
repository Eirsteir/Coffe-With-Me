package com.eirsteir.coffeewithme.gateway.security;

import com.eirsteir.coffeewithme.commons.security.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtConfig jwtConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
          http
                  .csrf().disable()
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                  .and()
                      .exceptionHandling()
                      .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                  .and()
                      .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                      .authorizeRequests()
                      .antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                      .antMatchers(HttpMethod.POST, jwtConfig.getUri() + "/register").permitAll()
                      .antMatchers( "/actuator/**").hasRole("ADMIN")
                      .antMatchers("/console/**").hasRole("ADMIN")
                      .antMatchers("/swagger-ui").permitAll()
                      .anyRequest().authenticated()
                  .and()
                      .headers().frameOptions().disable();
  }

  @Bean
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }

}
