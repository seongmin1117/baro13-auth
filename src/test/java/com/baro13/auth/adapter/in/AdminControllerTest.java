package com.baro13.auth.adapter.in;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baro13.auth.application.in.AuthService;
import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.domain.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private TokenPort tokenPort;

  @MockitoBean private AuthService authService;

  @DisplayName("관리자 권한 부여가 성공한다.")
  @Test
  void grantRole_success() throws Exception {
    // given
    Long userId = 1L;
    User mockUser = User.create("user1", "encoded-pass", "nickname");
    Mockito.when(authService.grantAdminRole(userId)).thenReturn(mockUser);

    String token = "mock-token";
    Mockito.when(tokenPort.validateToken(token)).thenReturn(true);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            "user1", null, List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));
    Mockito.when(tokenPort.extractAuthentication(token)).thenReturn(authentication);
    // when
    mockUser.grantAdminRole();

    ResultActions result =
        mockMvc.perform(
            patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    // then
    result
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.username").value("user1"))
        .andExpect(jsonPath("$.nickname").value("nickname"))
        .andExpect(jsonPath("$.roles[1]").value("ADMIN"));
  }

  @DisplayName("관리자가 아닌 경우 권한 부여가 실패한다.")
  @Test
  void grantRole_fail() throws Exception {
    // given
    Long userId = 1L;

    String token = "mock-token";
    Mockito.when(tokenPort.validateToken(token)).thenReturn(true);

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(
            "user1", null, List.of(new SimpleGrantedAuthority("ROLE_USER")));
    Mockito.when(tokenPort.extractAuthentication(token)).thenReturn(authentication);
    
    // when
    ResultActions result =
        mockMvc.perform(
            patch("/admin/users/{userId}/roles", userId)
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON));
    // then
    result
        .andExpect(status().isForbidden())
        .andExpect(jsonPath("$.error.code").value("ACCESS_DENIED"))
        .andExpect(jsonPath("$.error.message").value("접근 권한이 없습니다."));
  }
}
