package com.baro13.auth.application.in;

import com.baro13.auth.application.in.command.SignUpCommand;
import com.baro13.auth.application.out.UserPersistencePort;
import com.baro13.auth.domain.User;
import com.baro13.auth.exception.AuthException.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserPersistencePort userPersistencePort;
  private final PasswordEncoder passwordEncoder;

  public User signUp(final SignUpCommand command) {
    if (userPersistencePort.existsByUsername(command.username())) {
      throw new UserAlreadyExistsException();
    }
    String encodedPassword = passwordEncoder.encode(command.password());
    User user = User.create(command.username(), encodedPassword, command.nickname());
    return userPersistencePort.save(user);
  }
}
