package com.baro13.auth.adapter.in.dto.request;

import com.baro13.auth.application.in.command.SignUpCommand;
import jakarta.validation.constraints.NotBlank;

public record SignUpRequest(
    @NotBlank(message = "아이디는 필수입니다.") String username,
    @NotBlank(message = "비밀번호는 필수입니다.") String password,
    @NotBlank(message = "닉네임은 필수입니다.") String nickname) {
  public SignUpCommand toCommand() {
    return new SignUpCommand(username, password, nickname);
  }
}
