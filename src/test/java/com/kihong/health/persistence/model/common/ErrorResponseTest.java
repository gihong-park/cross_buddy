package com.kihong.health.persistence.model.common;

import static org.junit.jupiter.api.Assertions.*;

import com.kihong.health.web.exception.ErrorCode;
import com.kihong.health.web.exception.HttpException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

class ErrorResponseTest {

  @Test
  @DisplayName("EROR RESPONSE TRANSFER TEST")
  void toResponseEntity() {
    HttpException ex = new HttpException(ErrorCode.USER_NOT_FOUND, "This User doesn't exist");

    ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.toResponseEntity(ex);

    ErrorResponse errorResponse = ErrorResponse.builder()
        .status(ex.getErrorCode()
            .getHttpStatus()
            .value())
        .error(ex.getErrorCode()
            .getHttpStatus()
            .name())
        .code(ex.getErrorCode()
            .getErrorCode())
        .message(ex.getMessage())
        .details(ex.getErrorCode()
            .getDetail())
        .build();

    assertEquals(ErrorCode.USER_NOT_FOUND.getHttpStatus() ,responseEntity.getStatusCode());
    assertEquals(errorResponse.getStatus() ,responseEntity.getBody().getStatus());
    assertEquals(errorResponse.getError() ,responseEntity.getBody().getError());
    assertEquals(errorResponse.getCode() ,responseEntity.getBody().getCode());
    assertEquals(errorResponse.getMessage() ,responseEntity.getBody().getMessage());
    assertEquals(errorResponse.getDetails() ,responseEntity.getBody().getDetails());
  }
}