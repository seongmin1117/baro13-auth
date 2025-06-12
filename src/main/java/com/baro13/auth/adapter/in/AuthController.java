package com.baro13.auth.adapter.in;

import com.baro13.auth.adapter.in.dto.request.LoginRequest;
import com.baro13.auth.adapter.in.dto.request.SignUpRequest;
import com.baro13.auth.adapter.in.dto.response.LoginResponse;
import com.baro13.auth.adapter.in.dto.response.SignUpResponse;
import com.baro13.auth.application.in.AuthService;
import com.baro13.auth.application.in.command.LoginCommand;
import com.baro13.auth.application.in.command.SignUpCommand;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "로그인 및 회원가입 API")
@RestController
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
  @ApiResponse(responseCode = "201", description = "회원가입 성공")
  @PostMapping("signup")
  public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    SignUpCommand command = request.toCommand();
    SignUpResponse response = SignUpResponse.of(authService.signUp(command));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @Operation(summary = "로그인", description = "사용자 인증 후 JWT 토큰을 발급합니다.")
  @ApiResponse(responseCode = "200", description = "로그인 성공")
  @PostMapping("login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    LoginCommand command = request.toCommand();
    LoginResponse response = LoginResponse.of(authService.login(command));
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
