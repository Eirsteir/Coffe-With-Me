package com.eirsteir.coffeewithme.commons.exception;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExceptionResponse {

  private Date timestamp;
  private HttpStatus status;
  private String details;
  private String message;
  private List<String> errors;

  public ExceptionResponse(Date timestamp, String message, String details, HttpStatus status) {
    super();
    this.timestamp = timestamp;
    this.message = message;
    this.details = details;
    this.status = status;
  }

  public ExceptionResponse(
      Date timestamp, HttpStatus status, String details, String message, List<String> errors) {
    super();
    this.timestamp = timestamp;
    this.status = status;
    this.details = details;
    this.message = message;
    this.errors = errors;
  }

  public ExceptionResponse(
      Date timestamp, HttpStatus status, String details, String message, String error) {
    super();
    this.timestamp = timestamp;
    this.status = status;
    this.details = details;
    this.message = message;
    this.errors = Collections.singletonList(error);
  }
}
