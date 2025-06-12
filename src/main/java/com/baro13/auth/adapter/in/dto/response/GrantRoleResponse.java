package com.baro13.auth.adapter.in.dto.response;

import com.baro13.auth.domain.Role;
import com.baro13.auth.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record GrantRoleResponse(
    @Schema(description = "사용자 ID", example = "JIN HO")String username,
    @Schema(description = "닉네임", example = "Mentos")String nickname,
    @Schema(description = "권한", example = "[ADMIN]")List<Role> roles) {
    public static GrantRoleResponse of(User user) {
    return new GrantRoleResponse(user.getUsername(), user.getNickname(), user.getRoles());
    }
}
