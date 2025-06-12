package com.baro13.auth.adapter.in;

import com.baro13.auth.adapter.in.dto.request.SignUpRequest;
import com.baro13.auth.adapter.in.dto.response.SignUpResponse;
import com.baro13.auth.application.in.AuthService;
import com.baro13.auth.application.in.command.SignUpCommand;
import com.baro13.auth.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @PostMapping("signup")
  public ResponseEntity<SignUpResponse> signUp(@Valid @RequestBody SignUpRequest request) {
    SignUpCommand command = request.toCommand();
    SignUpResponse response = SignUpResponse.of(authService.signUp(command));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }
}
