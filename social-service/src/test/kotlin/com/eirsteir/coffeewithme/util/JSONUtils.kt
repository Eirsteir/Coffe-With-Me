package com.eirsteir.coffeewithme.util

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper

object JSONUtils {
    @Throws(JsonProcessingException::class)
    fun asJsonString(`object`: Any?): String? {
        val objectMapper = ObjectMapper()
        return objectMapper.writeValueAsString(`object`)
    }
}