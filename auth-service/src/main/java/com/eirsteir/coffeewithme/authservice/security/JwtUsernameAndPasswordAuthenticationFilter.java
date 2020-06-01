package com.eirsteir.coffeewithme.authservice.security;

import com.eirsteir.coffeewithme.authservice.domain.Account;
import com.eirsteir.coffeewithme.commons.security.JwtConfig;
import com.eirsteir.coffeewithme.commons.security.JwtUtils;
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsUtils;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;


@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    private final JwtUtils jwtUtils;

    private final JwtConfig jwtConfig;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager,
                                                      JwtUtils jwtUtils,
                                                      JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtUtils = jwtUtils;
        this.jwtConfig = jwtConfig;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (CorsUtils.isPreFlightRequest(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return null;
        }

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
                                            FilterChain chain, Authentication auth) throws IOException {

        log.debug("[x] Authentication principal: {}", auth.getPrincipal());

        UserDetailsImpl principal = (UserDetailsImpl) auth.getPrincipal();

        String token = jwtUtils.createJwtToken(auth, principal);

        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(JwtUtils.getResponseBody(token));
    }


}
