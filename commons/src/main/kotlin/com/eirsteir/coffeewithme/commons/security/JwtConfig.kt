package com.eirsteir.coffeewithme.commons.security

import org.springframework.beans.factory.annotation.Value

data class JwtConfig(
    @Value("\${security.jwt.uri:/auth}")
    val Uri: String? = null,
    @Value("\${security.jwt.header:Authorization}")
    val header: String? = null,
    @Value("\${security.jwt.prefix:Bearer }")
    val prefix: String? = null,
    @Value("\${security.jwt.expiration:#{24*60*60}}")
    val expiration: Int = 0,
    @Value("\${security.jwt.secret:JwtSecretKey}")
    val secret: String? = null,
)