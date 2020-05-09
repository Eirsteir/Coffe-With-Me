package com.eirsteir.coffeewithme.service;


import com.eirsteir.coffeewithme.domain.role.Role;
import com.eirsteir.coffeewithme.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@ToString
@Getter
@NoArgsConstructor
public class UserPrincipalImpl implements UserDetails {

  private static final long serialVersionUID = 7959303258679776171L;

  private User user;

  public UserPrincipalImpl(User user) {
    this.user = user;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : user.getRoles())
      authorities.add(new SimpleGrantedAuthority(role.getType().name()));

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
