package com.baro13.auth.adapter.in.dto.response;

import com.baro13.auth.domain.Role;
import com.baro13.auth.domain.User;
import java.util.List;

public record GrantRoleResponse(String username, String nickname, List<Role> roles) {
    public static GrantRoleResponse of(User user) {
    return new GrantRoleResponse(user.getUsername(), user.getNickname(), user.getRoles());
    }
}
