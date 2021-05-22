package com.eirsteir.coffeewithme.authservice.security

import com.eirsteir.coffeewithme.authservice.domain.Account
import com.eirsteir.coffeewithme.commons.security.JwtConfig
import com.eirsteir.coffeewithme.commons.security.JwtUtils
import com.eirsteir.coffeewithme.commons.security.UserDetailsImpl
import com.fasterxml.jackson.databind.ObjectMapper
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsUtils
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


private val logger = KotlinLogging.logger {}


class JwtUsernameAndPasswordAuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val jwtConfig: JwtConfig
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(
        request: HttpServletRequest, response: HttpServletResponse
    ): Authentication? {
        if (CorsUtils.isPreFlightRequest(request)) {
            response.status = HttpServletResponse.SC_OK
            return null
        }

        return try {
            val account = ObjectMapper().readValue(request.inputStream, Account::class.java)
            logger.debug("[x] Found credentials: $account")

            val authToken = UsernamePasswordAuthenticationToken(
                account.email, account.password, emptyList()
            )
            logger.debug("[x] Attempting to authenticate token: $authToken")
            authManager.authenticate(authToken)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        auth: Authentication
    ) {
        logger.debug("[x] Authentication principal: ${auth.principal}")

        val principal = auth.principal as UserDetailsImpl
        val token = JwtUtils.createJwtToken(jwtConfig, auth, principal)

        response.addHeader(jwtConfig.header, jwtConfig.prefix + token)
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        response.writer.write(token)
    }

    init {
        setRequiresAuthenticationRequestMatcher(
            AntPathRequestMatcher(jwtConfig.uri + "/login", "POST")
        )
    }
}