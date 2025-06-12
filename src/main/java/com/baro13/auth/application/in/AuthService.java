package com.baro13.auth.application.in;

import com.baro13.auth.application.in.command.LoginCommand;
import com.baro13.auth.application.in.command.SignUpCommand;
import com.baro13.auth.application.out.TokenPort;
import com.baro13.auth.application.out.UserPersistencePort;
import com.baro13.auth.domain.User;
import com.baro13.auth.exception.AuthException.InvalidCredentialsException;
import com.baro13.auth.exception.AuthException.UserAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserPersistencePort userPersistencePort;
  private final PasswordEncoder passwordEncoder;
  private final TokenPort tokenPort;

  public User signUp(final SignUpCommand command) {
    if (userPersistencePort.existsByUsername(command.username())) {
      throw new UserAlreadyExistsException();
    }
    String encodedPassword = passwordEncoder.encode(command.password());
    User user = User.create(command.username(), encodedPassword, command.nickname());
    return userPersistencePort.save(user);
  }

  public String login(final LoginCommand command) {
    User user = userPersistencePort.findByUsername(command.username());
    if (user == null) {
      throw new InvalidCredentialsException();
    }
    if (!passwordEncoder.matches(command.password(), user.getPassword())) {
      throw new InvalidCredentialsException();
    }
    return tokenPort.generateAccessToken(user.getId(),user.getRoles());
  }
}
