package com.eirsteir.coffeewithme.commons.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtUtils {

  public static String createJwtToken(
      JwtConfig jwtConfig, Authentication auth, UserDetailsImpl principal) {
    long now = System.currentTimeMillis();
    return Jwts.builder()
        .setSubject(principal.getId().toString())
        .claim("user_id", principal.getId().toString())
        .claim("email", principal.getEmail()) // TODO: 02.06.2020 remove?
        .claim("nickname", principal.getNickname()) // TODO: 02.06.2020 remove?
        .claim(
            "authorities",
            auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()))
        .setIssuedAt(new Date(now))
        .setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
        .signWith(SignatureAlgorithm.HS256, jwtConfig.getSecret().getBytes())
        .compact();
  }
}
