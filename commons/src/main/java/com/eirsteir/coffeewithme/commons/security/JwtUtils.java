package com.eirsteir.coffeewithme.commons.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtils {

    private static JwtConfig jwtConfig;

    public JwtUtils(JwtConfig jwtConfig) {
        JwtUtils.jwtConfig = jwtConfig;
    }

    public String createJwtToken(Authentication auth, UserDetailsImpl principal) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(principal.getId().toString())
                .claim("email", principal.getEmail())
                .claim("nickname", principal.getNickname())
                .claim("authorities", auth.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
                .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes())
                .compact();
    }

    public static String getResponseBody(String token) {
        return "{\"access_token\":\"" + jwtConfig.getPrefix() + token + "\"," +
                "\"token_type\": \"bearer\"," +
                "\"expires_in\": \"" + jwtConfig.getExpiration() + "\"," +
                "}";
    }

}
