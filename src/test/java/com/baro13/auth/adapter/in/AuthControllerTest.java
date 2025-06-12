package com.baro13.auth.adapter.in;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.baro13.auth.adapter.in.dto.request.LoginRequest;
import com.baro13.auth.adapter.in.dto.request.SignUpRequest;
import com.baro13.auth.application.in.AuthService;
import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.domain.User;
import com.baro13.auth.exception.AuthException.InvalidCredentialsException;
import com.baro13.auth.exception.AuthException.UserAlreadyExistsException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @MockitoBean private TokenPort tokenPort;

  @MockitoBean private AuthService authService;

  @DisplayName("회원가입이 성공한다.")
  @Test
  void signUp_success() throws Exception {
    // given
    SignUpRequest request = new SignUpRequest("JIN HO", "12341234", "Mentos");
    User mockUser = User.create("user1", "encoded-pass", "nickname");
    Mockito.when(authService.signUp(request.toCommand())).thenReturn(mockUser);

    // when
    ResultActions result =
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

    // then
    result
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.username").value("user1"))
        .andExpect(jsonPath("$.nickname").value("nickname"));
  }

  @DisplayName("회원가입 시 이미 가입된 사용자면 회원가입이 실패한다.")
  @Test
  void signUp_fail_userAlreadyExists() throws Exception {
    // given
    SignUpRequest request = new SignUpRequest("JIN HO", "12341234", "Mentos");

    Mockito.when(authService.signUp(request.toCommand()))
        .thenThrow(new UserAlreadyExistsException());

    // when
    ResultActions result =
        mockMvc.perform(
            post("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

    // then
    result
        .andExpect(status().isConflict())
        .andExpect(jsonPath("$.error.code").value("USER_ALREADY_EXISTS"))
        .andExpect(jsonPath("$.error.message").value("이미 가입된 사용자입니다."));
  }

  @DisplayName("로그인이 성공한다.")
  @Test
  void login_success() throws Exception {
    // given
    LoginRequest request = new LoginRequest("JIN HO", "12341234");
    String token = "test-token";
    Mockito.when(authService.login(request.toCommand())).thenReturn(token);

    // when
    ResultActions result =
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

    // then
    result.andExpect(status().isOk()).andExpect(jsonPath("$.token").value("test-token"));
  }

  @DisplayName("아이디와 패스워드가 일치하지 않으면 로그인이 실패한다.")
  @Test
  void login_fail() throws Exception {
    // given
    LoginRequest request = new LoginRequest("JIN HO", "12341234");
    Mockito.when(authService.login(request.toCommand())).thenThrow(new InvalidCredentialsException());

    // when
    ResultActions result =
        mockMvc.perform(
            post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
    // then
    result.andExpect(status().isUnauthorized())
        .andExpect(jsonPath("$.error.code").value("INVALID_CREDENTIALS"))
        .andExpect(jsonPath("$.error.message").value("아이디 또는 비밀번호가 올바르지 않습니다."));
    ;
    }
}
