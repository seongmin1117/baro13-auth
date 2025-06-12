package com.baro13.auth.common.response;

import com.baro13.auth.common.code.ErrorCode;

public record ErrorResponse(ErrorDetail error) {

  public static ErrorResponse of(ErrorCode errorCode) {
    return new ErrorResponse(new ErrorDetail(errorCode.name(), errorCode.getMessage()));
  }

  public record ErrorDetail(String code, String message) {}
}
