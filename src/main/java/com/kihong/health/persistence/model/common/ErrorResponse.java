package com.kihong.health.persistence.model.common;

import com.kihong.health.web.exception.HttpException;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {
  private final String timestamp = LocalDateTime.now().toString();
  private final int status;
  private final String error;
  private final String code;
  private final String message;
  private final String details;
  private final String at;

  public static ResponseEntity toResponseEntity(HttpException ex) {
    return ResponseEntity
        .status(ex.getErrorCode()
            .getHttpStatus())
        .body(ErrorResponse.builder()
            .status(ex.getErrorCode()
                .getHttpStatus()
                .value())
            .error(ex.getErrorCode()
                .getHttpStatus()
                .name())
            .code(ex.getErrorCode()
                .getErrorCode())
            .message(ex.getErrorCode()
                .getDetail())
            .details(ex.getMessage())
            .build()
        );
  }

}
