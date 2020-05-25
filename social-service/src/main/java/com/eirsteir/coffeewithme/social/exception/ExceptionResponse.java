package com.eirsteir.coffeewithme.social.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
public class ExceptionResponse {

    private Date timestamp;
    private String message;
    private String details;
    private String status;

    public ExceptionResponse(Date timestamp, String message, String details, HttpStatus status) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
        this.status = status.toString();
    }
}

