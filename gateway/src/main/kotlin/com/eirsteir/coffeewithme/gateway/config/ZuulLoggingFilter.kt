package com.eirsteir.coffeewithme.gateway.config

import com.netflix.zuul.ZuulFilter
import com.netflix.zuul.context.RequestContext
import com.netflix.zuul.exception.ZuulException
import lombok.extern.slf4j.Slf4j
import mu.KotlinLogging
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest

private val logger = KotlinLogging.logger {  }

@Component
class ZuulLoggingFilter : ZuulFilter() {

    override fun filterType() = FilterConstants.PRE_TYPE

    override fun filterOrder() = 1

    override fun shouldFilter() = true

    /** Log the content of the request  */
    override fun run(): Any? {
        val request = RequestContext.getCurrentContext().request
        logger.info("[x] Request: ${request.method}, URI: ${request.requestURI}")
        logger.info("[x] Body: ${tryToGetRequestBody(request)}")
        return null
    }

    private fun tryToGetRequestBody(request: HttpServletRequest) =
        try {
            getRequestBody(request)
        } catch (e: IOException) {
            logger.warn("[x] Failed to get request body")
            ""
        }

    private fun getRequestBody(request: HttpServletRequest) =
        request.reader.lines().collect(Collectors.joining(System.lineSeparator()))
}