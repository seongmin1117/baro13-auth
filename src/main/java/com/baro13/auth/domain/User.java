package com.baro13.auth.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class User {
  private Long id;
  private final String username;
  private final String password;
  private final String nickname;
  private final List<Role> roles;

  private User(String username, String password, String nickname, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.roles = new ArrayList<>(roles);
  }

  public static User create(String username, String password, String nickname) {
    return new User(username, password, nickname, List.of(Role.USER));
  }

  public User grantAdminRole() {
    if (!roles.contains(Role.ADMIN)) {
      roles.add(Role.ADMIN);
    }
    return this;
  }
}
