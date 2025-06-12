package com.baro13.auth.application.out;

import com.baro13.auth.domain.Role;
import java.util.List;
import org.springframework.security.core.Authentication;

public interface TokenPort {
    String generateAccessToken(Long userId, List<Role> roles);
    boolean validateToken(String token);
    Authentication extractAuthentication(String token);
}
