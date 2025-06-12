package com.baro13.auth.adapter.in.dto.response;

import com.baro13.auth.domain.Role;
import com.baro13.auth.domain.User;
import java.util.List;

public record SignUpResponse(String username, String nickname, List<Role> roles) {
  public static SignUpResponse of(User user) {
    return new SignUpResponse(user.getUsername(), user.getNickname(), user.getRoles());
  }
}
