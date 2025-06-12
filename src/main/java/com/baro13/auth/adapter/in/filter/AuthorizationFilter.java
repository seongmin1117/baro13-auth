package com.baro13.auth.adapter.in.filter;

import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.common.code.ErrorCode;
import com.baro13.auth.common.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final List<String> WHITELIST = List.of(
      "/login",
      "/signup",
      "/swagger-ui/**",
      "/v3/api-docs/**"
  );

  private final TokenPort tokenPort;
  private final ObjectMapper objectMapper;
  private final PathMatcher pathMatcher;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws ServletException, IOException {

    String requestURI = request.getRequestURI();

    if (isWhitelisted(requestURI)) {
      chain.doFilter(request, response);
      return;
    }

    try {
      String token = extractToken(request);
      handleAuthentication(token);
      chain.doFilter(request, response);

    } catch (InvalidTokenException e) {
      setErrorResponse(response, ErrorCode.INVALID_TOKEN);
    }
  }

  private boolean isWhitelisted(String requestURI) {
    return WHITELIST.stream().anyMatch(pattern -> pathMatcher.match(pattern, requestURI));
  }

  private String extractToken(HttpServletRequest request) {
    String header = request.getHeader(AUTHORIZATION_HEADER);
    if (header == null || !header.startsWith(BEARER_PREFIX)) {
      throw new InvalidTokenException();
    }
    return header.substring(BEARER_PREFIX.length());
  }

  private void handleAuthentication(String token) {
    if (!tokenPort.validateToken(token)) {
      throw new InvalidTokenException();
    }
    Authentication authentication = tokenPort.extractAuthentication(token);
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

  private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
    response.setStatus(errorCode.getHttpStatus().value());
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");

    ErrorResponse errorResponse = ErrorResponse.of(errorCode);
    response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
  }

  private static class InvalidTokenException extends RuntimeException {}
}
