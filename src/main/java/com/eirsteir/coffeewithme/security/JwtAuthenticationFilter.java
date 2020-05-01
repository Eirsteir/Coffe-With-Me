package com.eirsteir.coffeewithme.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String TOKEN_PARAM = "token";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private static Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        String token = req.getHeader(TOKEN_PARAM);
        String email = null;
        System.out.println(token);

        if (token != null) {
            try {
                email = jwtTokenUtil.getEmailFromToken(token);
                System.out.println(email);
            } catch (IllegalArgumentException e) {
                logger.error("An error occurred when getting email from token.", e);
            } catch (ExpiredJwtException e) {
                logger.warn("The token has expired and not valid anymore", e);
            } catch(SignatureException e){
                logger.error("Authentication Failed. Username or Password not valid.");
            }
        } else {
            logger.warn("Could not find bearer string, will ignore the header.");
        }
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            System.out.println(email);
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                                                                                             null,
                                                                                                             Collections.singletonList(
                                                                                                                     new SimpleGrantedAuthority(
                                                                                                                             "ROLE_USER")));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                logger.info("Authenticated user {}, setting security context.", email);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(req, res);
    }
}