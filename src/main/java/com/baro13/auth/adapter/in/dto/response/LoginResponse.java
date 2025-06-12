package com.baro13.auth.adapter.in.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
    @Schema(description = "Jwt토큰", example = "abcd1234qwer5678zxcv910") String token) {
  public static LoginResponse of(String token) {
    return new LoginResponse(token);
  }
}
