package com.baro13.auth.adapter.out.token;

import com.baro13.auth.adapter.out.token.provider.JwtTokenProvider;
import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.domain.Role;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenAdapter implements TokenPort {
  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public String generateAccessToken(Long userId, List<Role> roles) {
    String subject = String.valueOf(userId);
    List<String> authorities = roles.stream().map(Role::getAuthority).toList();
    return jwtTokenProvider.generateAccessToken(subject, authorities);
  }

  @Override
  public boolean validateToken(String token) {
    return false;
  }

  @Override
  public Authentication extractAuthentication(String token) {
    return null;
  }
}
