package demo.security;

import demo.model.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Getter
@ToString
public class CMEUserPrincipal implements UserDetails {

  private User user;

  public CMEUserPrincipal(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.<GrantedAuthority>singletonList(new SimpleGrantedAuthority("USER"));
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return !user.getAccountExpired();
  }

  @Override
  public boolean isAccountNonLocked() {
    return !user.getAccountLocked();
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return !user.getCredentialsExpired();
  }

  @Override
  public boolean isEnabled() {
    return user.getEnabled();
  }
}
