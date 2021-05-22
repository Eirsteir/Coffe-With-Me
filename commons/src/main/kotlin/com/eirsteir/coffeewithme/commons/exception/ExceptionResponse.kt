package com.eirsteir.coffeewithme.commons.exception

import org.springframework.http.HttpStatus
import java.time.LocalDateTime

data class ExceptionResponse(
    private var timestamp: LocalDateTime = LocalDateTime.now(),
    var status: HttpStatus,
    private var details: String,
    private var message: String? = "Something went wrong",
    private var errors: List<String> = listOf()
)