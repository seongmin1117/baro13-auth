package com.baro13.auth.adapter.out.token.provider;

import com.baro13.auth.common.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
  private static final long EXPIRATION_TIME_MS = 1000 * 60 * 60;
  private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
  private final JwtConfig jwtConfig;

  public String generateAccessToken(String subject, List<String> roles) {
    Date now = new Date();
    Date expiry = new Date(now.getTime() + EXPIRATION_TIME_MS);
    
    return Jwts.builder()
        .setSubject(subject)
        .claim("roles", roles)
        .setIssuedAt(now)
        .setExpiration(expiry)
        .signWith(jwtConfig.getKey(), SIGNATURE_ALGORITHM)
        .compact();
  }
}
