package com.baro13.auth.adapter.in.dto.response;

import com.baro13.auth.domain.Role;
import com.baro13.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record SignUpResponse(
    @Schema(description = "사용자 ID", example = "Mentos")String username,
    @Schema(description = "닉네임", example = "Mentos")String nickname,
    @Schema(description = "권한", example = "[USER]")List<Role> roles) {
  public static SignUpResponse of(User user) {
    return new SignUpResponse(user.getUsername(), user.getNickname(), user.getRoles());
  }
}
