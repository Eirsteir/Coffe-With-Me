package com.eirsteir.coffeewithme.service;


import com.eirsteir.coffeewithme.domain.user.Role;
import com.eirsteir.coffeewithme.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@NoArgsConstructor
public class CMEUserPrincipal implements UserDetails {

  private User user;

  public CMEUserPrincipal(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : user.getRoles())
      authorities.add(new SimpleGrantedAuthority(role.getName()));

    return authorities;
  }

  @Override
  public String getPassword() {
    return user.getPassword();
  }

  @Override
  public String getUsername() {
    return user.getName();
  }

  public String getEmail() {
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
