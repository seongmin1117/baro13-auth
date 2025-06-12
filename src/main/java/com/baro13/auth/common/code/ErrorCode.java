package com.baro13.auth.common.code;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
  USER_ALREADY_EXISTS("이미 가입된 사용자입니다.", HttpStatus.CONFLICT),
  INVALID_REQUEST("요청 값이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
  USER_NOT_FOUND("사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INTERNAL_SERVER_ERROR("서버 내부 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

  private final String message;
  private final HttpStatus httpStatus;

  ErrorCode(String message, HttpStatus httpStatus) {
    this.message = message;
    this.httpStatus = httpStatus;
  }

  public String getCode() {
    return name();
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getHttpStatus() {
    return httpStatus;
  }
}
