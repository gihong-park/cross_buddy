package com.kihong.health.web.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
  BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "잘못된 포멧입니다."),
  TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "TOKEN_EXPIRED", "토큰이 만료 되었습니다."),
  RECORD_NOT_FOUND(HttpStatus.NOT_FOUND, "RECORD_404", "찾는 record가 존재하지 않습니다."),
  WOD_NOT_FOUND(HttpStatus.NOT_FOUND, "WOD_404", "찾는 wod가 존재하지 않습니다."),
  USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "USER_401", "로그인을 해주시기 바랍니다."),
  USER_FORBIDDEN(HttpStatus.FORBIDDEN, "USER_403", "권한이 없습니다."),
  CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "이미 존재합니다."),
  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_404", "찾는 user가 존재하지 않습니다.");
  private final HttpStatus httpStatus;
  private final String errorCode;
  private final String detail;
}
