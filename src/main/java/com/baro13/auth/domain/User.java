package com.baro13.auth.domain;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class User {
  private final String username;
  private final String password;
  private final String nickname;
  private final List<Role> roles;

  private User(String username, String password, String nickname, List<Role> roles) {
    this.username = username;
    this.password = password;
    this.nickname = nickname;
    this.roles = List.copyOf(roles);
  }

  public static User create(String username, String password, String nickname) {
    return new User(username, password, nickname, List.of(Role.USER));
  }
}
