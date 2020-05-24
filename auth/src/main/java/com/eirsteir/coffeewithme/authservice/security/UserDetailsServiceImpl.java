package com.eirsteir.coffeewithme.authservice.security;

import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.UserCredentials;
import com.eirsteir.coffeewithme.authservice.repository.UserCredentialsRepository;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserCredentialsRepository repository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserCredentials userCredentials = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("[x] Email: " + email + " not found"));

        return UserDetailsImpl.builder()
                .cwmId(userCredentials.getCwmId())
                .email(userCredentials.getEmail())
                .password(userCredentials.getPassword())
                .roles(getAuthorities(userCredentials))
                .build();
    }

    public Collection<? extends GrantedAuthority> getAuthorities(UserCredentials userCredentials) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : userCredentials.getRoles())
            authorities.add(new SimpleGrantedAuthority(role.getType().name()));

        return authorities;
    }
}
