package com.eirsteir.coffeewithme.security;


import com.eirsteir.coffeewithme.domain.role.Role;
import com.eirsteir.coffeewithme.domain.user.User;
import com.eirsteir.coffeewithme.exception.CWMException;
import com.eirsteir.coffeewithme.exception.EntityType;
import com.eirsteir.coffeewithme.exception.ExceptionType;
import com.eirsteir.coffeewithme.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) {
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> CWMException.getException(
                    EntityType.USER, ExceptionType.ENTITY_NOT_FOUND, email));

    log.info("[x] Loaded user {}", user);
//    return new UserPrincipalImpl(user);
    return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                                                                  getAuthorities(user));
  }

  public Collection<? extends GrantedAuthority> getAuthorities(User user) {
    List<GrantedAuthority> authorities = new ArrayList<>();
    for (Role role : user.getRoles())
      authorities.add(new SimpleGrantedAuthority(role.getType().name()));

    return authorities;
  }

}
