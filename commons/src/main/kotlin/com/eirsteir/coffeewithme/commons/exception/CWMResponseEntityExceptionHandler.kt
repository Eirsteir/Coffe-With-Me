package com.eirsteir.coffeewithme.commons.exception

import com.eirsteir.coffeewithme.commons.exception.CWMException.DuplicateEntityException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.*


@RestControllerAdvice
class CWMResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(Exception::class)
    fun handleAllException(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        val exceptionResponse = ExceptionResponse(
            message = ex.message,
            details = request.getDescription(false),
            status = HttpStatus.INTERNAL_SERVER_ERROR
        )
        logger.error("[x] An error occurred: ", ex)
        return ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(CWMException.EntityNotFoundException::class)
    fun handleEntityNotFoundException(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any> {
        val exceptionResponse = ExceptionResponse(
            message = ex.message,
            details = request.getDescription(false),
            status = HttpStatus.NOT_FOUND
        )
        logger.error("[x] Entity does not exist: ", ex)
        return ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(DuplicateEntityException::class)
    fun handleDuplicateEntityException(
        ex: Exception, request: WebRequest
    ): ResponseEntity<Any> {
        val exceptionResponse = ExceptionResponse(
            message = ex.message,
            details = request.getDescription(false),
            status = HttpStatus.BAD_REQUEST
        )
        logger.error("[x] Entity already exist: ", ex)
        return ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ResponseStatusException::class)
    fun handleResponseStatusException(
        ex: ResponseStatusException, request: WebRequest
    ): ResponseEntity<Any> {
        val exceptionResponse = ExceptionResponse(
            message =  ex.reason,
            details = request.getDescription(false),
            status = ex.status
        )
        logger.error(
            "[x] An API error with status code ${ex.status} occurred: ",
            ex
        )
        return ResponseEntity(exceptionResponse, ex.status)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatus,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors: MutableList<String> = ArrayList()

        for (error in ex.bindingResult.fieldErrors)
            errors.add(error.field + ": " + error.defaultMessage)

        for (error in ex.bindingResult.globalErrors)
            errors.add(error.objectName + ": " + error.defaultMessage)

        val exceptionResponse = ExceptionResponse(
            status = HttpStatus.BAD_REQUEST,
            details = "Validation failed",
            message = ex.localizedMessage,
            errors = errors
        )
        logger.error("[x] Validation failed: $errors", ex)

        return handleExceptionInternal(
            ex, exceptionResponse, headers, exceptionResponse.status, request
        )
    }
}