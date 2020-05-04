package demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
    // @formatter:off
//      auth.userDetailsService()
    // TODO: 02.05.2020 change from in memory authentication?
          auth.inMemoryAuthentication()
              .withUser("user").password("password").roles("USER")
          .and()
              .withUser("admin").password("admin").roles("USER", "ADMIN", "READER", "WRITER")
          .and()
              .withUser("audit").password("audit").roles("USER", "ADMIN", "READER");

// @formatter:on
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // @formatter:off
          http
              .httpBasic().and()
              .logout().and()
              .authorizeRequests()
                  .antMatchers("/index.html", "/").permitAll()
                  .anyRequest().authenticated()
                  .and()
              .csrf()
                  .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
          // @formatter:on
  }
}
