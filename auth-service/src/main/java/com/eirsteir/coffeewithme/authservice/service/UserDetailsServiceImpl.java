package com.eirsteir.coffeewithme.authservice.service;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired private AccountRepository repository;

  @Override
  public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
    Account account =
        repository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("[x] Email: " + email + " not found"));

    return UserDetailsImpl.builder()
        .id(account.getId())
        .email(account.getEmail())
        .password(account.getPassword())
        .roles(getAuthorities(account))
        .build();
  }

  public Collection<? extends GrantedAuthority> getAuthorities(Account account) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : account.getRoles())
      authorities.add(new SimpleGrantedAuthority(role.getType().name()));

    return authorities;
  }
}
