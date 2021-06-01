package com.eirsteir.coffeewithme.commons.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import java.util.*
import java.util.stream.Collectors

class JwtUtils {

    companion object {
        fun createJwtToken(
            jwtConfig: JwtConfig, auth: Authentication, principal: UserDetailsImpl
        ): String {
            val now = System.currentTimeMillis()
            return Jwts.builder()
                .setSubject(principal.id.toString())
                .claim("user_id", principal.id.toString())
                .claim("email", principal.email) // TODO: 02.06.2020 remove?
                .claim("nickname", principal.nickname) // TODO: 02.06.2020 remove?
                .claim(
                    "authorities",
                    auth.authorities.stream()
                        .map { obj: GrantedAuthority -> obj.authority }
                        .collect(Collectors.toList()))
                .setIssuedAt(Date(now))
                .setExpiration(Date(now + jwtConfig.expiration * 1000))
                .signWith(SignatureAlgorithm.HS256, jwtConfig.secret.toByteArray())
                .compact()
        }
    }
}