package com.eirsteir.coffeewithme.authservice.security;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.commons.security.JwtConfig;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.stream.Collectors;


@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            Account account = new ObjectMapper().readValue(request.getInputStream(), Account.class);
            log.debug("[x] Found credentials: {}", account);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    account.getEmail(), account.getPassword(), Collections.emptyList());

            log.debug("[x] Attempting to authenticate token: {}", authToken);
            return authManager.authenticate(authToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication auth)
            throws IOException, ServletException {

        log.debug("[x] Authentication principal: {}", auth.getPrincipal());

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        long now = System.currentTimeMillis();
        String token = Jwts.builder()
                .setSubject(principal.getCwmId().toString())
                .claim("email", principal.getEmail())
                .claim("username", principal.getUsername())
                .claim("authorities", auth.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();

        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
    }

}
