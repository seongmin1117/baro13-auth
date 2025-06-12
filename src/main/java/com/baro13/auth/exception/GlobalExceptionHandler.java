package com.baro13.auth.exception;

import com.baro13.auth.common.code.ErrorCode;
import com.baro13.auth.common.response.ErrorResponse;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  protected ResponseEntity<ErrorResponse> handleBadRequestException(BusinessException e) {
    final ErrorCode errorCode = e.getErrorCode();
    return ResponseEntity.status(errorCode.getHttpStatus()).body(ErrorResponse.of(errorCode));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e) {
    String message =
        e.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .orElse(ErrorCode.INVALID_REQUEST.getMessage());

    return ResponseEntity.badRequest()
        .body(
            new ErrorResponse(
                new ErrorResponse.ErrorDetail(ErrorCode.INVALID_REQUEST.getCode(), message)));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
  }
}
