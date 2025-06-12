package com.baro13.auth.exception;

import com.baro13.auth.common.code.ErrorCode;

public class AuthException extends BusinessException {
  public AuthException(ErrorCode errorCode) {
    super(errorCode);
  }

  public static class UserAlreadyExistsException extends AuthException {
    public UserAlreadyExistsException() {
      super(ErrorCode.USER_ALREADY_EXISTS);
    }
  }

  public static class UserNotFountException extends AuthException {
    public UserNotFountException() {
      super(ErrorCode.USER_NOT_FOUND);
    }
  }

  public static class InvalidCredentialsException extends AuthException {
    public InvalidCredentialsException() {
      super(ErrorCode.INVALID_CREDENTIALS);
    }
  }
}
