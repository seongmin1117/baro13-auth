package com.baro13.auth.domain;

public enum Role {
  USER,
  ADMIN;

  private static final String PREFIX = "ROLE_";

  public String getAuthority() {
    return PREFIX + name();
  }
}
