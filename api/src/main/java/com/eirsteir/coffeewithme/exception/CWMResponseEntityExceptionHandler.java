package com.eirsteir.coffeewithme.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
@RestController
public class CWMResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                                                                    request.getDescription(false),
                                                                    HttpStatus.INTERNAL_SERVER_ERROR);
        log.error("[x] An error occurred: ", ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CWMException.EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                                                                    request.getDescription(false),
                                                                    HttpStatus.NOT_FOUND);
        log.error("[x] Entity does not exist: ", ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(CWMException.DuplicateEntityException.class)
    public final ResponseEntity<Object> handleDuplicateEntityException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                                                                    request.getDescription(false),
                                                                    HttpStatus.BAD_REQUEST);
        log.error("[x] Entity already exist: ", ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public final ResponseEntity<Object> handleResponseStatusException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                                                                    request.getDescription(false),
                                                                    HttpStatus.BAD_REQUEST);

        log.error("[x] An error with status code {} occurred in the API: ", HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        String errors = getErrorsFrom(ex);

        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(),
                                                                    "Validation failed",
                                                                    errors,
                                                                    status);
        log.error("[x] Validation failed: {}", errors, ex);
        return new ResponseEntity<>(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

    private String getErrorsFrom(MethodArgumentNotValidException ex) {
        return ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .map(m -> m.replace(".", ""))
                .collect(Collectors.joining(", "));
    }
}
