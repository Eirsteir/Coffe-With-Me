package com.eirsteir.coffeewithme.gateway.security

import com.eirsteir.coffeewithme.commons.security.JwtConfig
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import io.jsonwebtoken.Jwts
import lombok.AllArgsConstructor
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtTokenAuthenticationFilter(private val jwtConfig: JwtConfig) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val header = request.getHeader(jwtConfig.header)
        logger.debug("[x] Filtering Jwt from request header: $header")

        if (!header.startsWith(jwtConfig.prefix)) {
            chain.doFilter(request, response)
            return
        }

        val token = header.replace(jwtConfig.prefix, "")

        try {
            val claims = Jwts.parser()
                .setSigningKey(jwtConfig.secret.toByteArray())
                .parseClaimsJws(token)
                .body

            val id = claims.subject

            val authorities = claims["authorities"] as MutableList<String>

            val principal = UserDetailsImpl(
                id = id.toLong(),
                email = claims["email"] as String,
                nickname = claims["nickname"] as String
            )
            val auth = UsernamePasswordAuthenticationToken(
                principal,
                null,
                authorities.stream().map { role: String? -> SimpleGrantedAuthority(role) }
                    .collect(Collectors.toList())
            )

            logger.info("[x] Authenticating user: $auth")

            SecurityContextHolder.getContext().authentication = auth
        } catch (e: Exception) {
            logger.error(
                "[x] Error while authenticating user, clearing security context: ",
                e
            )
            SecurityContextHolder.clearContext()
        }
        chain.doFilter(request, response)
    }
}