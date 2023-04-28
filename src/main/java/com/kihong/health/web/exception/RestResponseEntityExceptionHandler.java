package com.kihong.health.web.exception;

import com.kihong.health.persistence.model.common.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  public RestResponseEntityExceptionHandler() {
    super();
  }

  @ExceptionHandler({HttpException.class})
  public ResponseEntity handleHttpException(final HttpException ex, final WebRequest request) {
    return ErrorResponse.toResponseEntity(ex);
  }
}
