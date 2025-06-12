package com.baro13.auth.adapter.out.token;

import com.baro13.auth.adapter.out.token.provider.JwtTokenProvider;
import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.domain.Role;
import io.jsonwebtoken.Claims;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
    return jwtTokenProvider.validateToken(token);
  }

  @Override
  public Authentication extractAuthentication(String token) {
    try {
      Claims claims = jwtTokenProvider.parseClaims(token);
      String userId = claims.getSubject();

      if (userId == null) {
        throw new IllegalStateException("토큰에 유저아이디가 없습니다.");
      }

      @SuppressWarnings("unchecked")
      List<String> roles = claims.get("roles", List.class);
      if (roles == null || roles.isEmpty()) {
        throw new IllegalStateException("토큰에 권한 정보가 없습니다.");
      }

      List<SimpleGrantedAuthority> authorities = roles.stream()
          .map(SimpleGrantedAuthority::new)
          .toList();

      return new UsernamePasswordAuthenticationToken(userId, null, authorities);
    } catch (Exception e) {
      throw new IllegalStateException("토큰에서 인증 정보를 추출할 수 없습니다.", e);
    }
  }

}
