package com.baro13.auth.adapter.in.dto.request;

import com.baro13.auth.application.in.command.LoginCommand;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @Schema(description = "사용자 ID", example = "JIN HO")
    @NotBlank(message = "아이디는 필수입니다.")String username,

    @Schema(description = "비밀번호", example = "12341234")
    @NotBlank(message = "비밀번호는 필수입니다.") String password) {
    public LoginCommand toCommand() {
        return new LoginCommand(username, password);
    }
}
