package com.kihong.health.web.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HttpException extends RuntimeException {

  private final ErrorCode errorCode;
  private final String message;
}
