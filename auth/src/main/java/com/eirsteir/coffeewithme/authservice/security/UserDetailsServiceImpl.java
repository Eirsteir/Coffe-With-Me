package com.eirsteir.coffeewithme.authservice.security;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.authservice.domain.Role;
import com.eirsteir.coffeewithme.authservice.domain.RoleType;
import com.eirsteir.coffeewithme.authservice.repository.AccountRepository;
import com.eirsteir.coffeewithme.authservice.service.UserService;
import com.eirsteir.coffeewithme.authservice.web.request.UserRegistrationRequest;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private AccountRepository repository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private WebClient webClient;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("[x] Email: " + email + " not found"));

        return UserDetailsImpl.builder()
                .cwmId(account.getCwmId())
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

    // TODO: 24.05.2020 throw exception?
    @Override
    public Optional<Account> registerUser(UserRegistrationRequest registrationRequest) {
        Optional<Account> user = repository.findByEmail(registrationRequest.getEmail());

        if (user.isPresent())
            return Optional.empty();

        Account account = repository.save(createAccount(registrationRequest));
        log.info("[x] Registered account: {}", account);

        return Optional.of(account);
    }

    private Account createAccount(UserRegistrationRequest registrationRequest) {
        return Account.builder()
                .email(registrationRequest.getEmail())
                .username(registrationRequest.getUsername())
                .roles(Collections.singletonList(Role.builder().type(RoleType.ROLE_USER).build()))
                .password(encoder.encode(registrationRequest.getPassword()))
                .build();
    }

    @Override
    public void dispatch(Account account) {
        String response = webClient.post()
                .uri("/user")
                .body(BodyInserters.fromPublisher(Mono.just(account), Account.class))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .block()
                .bodyToMono(String.class)
                .block();

        log.debug("[x] Received response from api: {}", response);
    }
}
