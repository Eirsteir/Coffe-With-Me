package com.eirsteir.coffeewithme.commons.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils {

    public static String createJwtToken(JwtConfig jwtConfig, Authentication auth, UserDetailsImpl principal) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(principal.getId().toString())
                .claim("user_id", principal.getId().toString())
                .claim("email", principal.getEmail())
                .claim("nickname", principal.getNickname())
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret().getBytes())
                .compact();
    }

}
