package com.eirsteir.coffeewithme.gateway.security;

import com.eirsteir.coffeewithme.commons.security.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@PropertySource("classpath:env.properties")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private JwtConfig jwtConfig;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
          http
                .cors()
              .and()
                  .csrf().disable()
                  .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
                  .exceptionHandling()
                  .authenticationEntryPoint((req, res, e) -> res.sendError(HttpServletResponse.SC_UNAUTHORIZED))
              .and()
                  .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
                  .authorizeRequests()
                  .antMatchers(HttpMethod.POST, jwtConfig.getUri() + "/**").permitAll()
                  .antMatchers( "/actuator/**").hasRole("ADMIN")
                  .anyRequest().authenticated();
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowCredentials(true);
    config.addAllowedOrigin("*");  // TODO: lock down before deploying
    config.addAllowedHeader("*");
    config.addExposedHeader(HttpHeaders.AUTHORIZATION);
    config.addAllowedMethod("*");
    source.registerCorsConfiguration("/**", config);
    return new CorsFilter(source);
  }

  @Bean
  public JwtConfig jwtConfig() {
    return new JwtConfig();
  }


  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs",
                               "/configuration/ui",
                               "/swagger-resources/**",
                               "/configuration/security",
                               "/swagger-ui.html",
                               "/webjars/**");
  }
}
