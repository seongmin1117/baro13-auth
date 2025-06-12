package com.baro13.auth.adapter.out.persistence;

import com.baro13.auth.adapter.out.persistence.repository.UserMemoryRepository;
import com.baro13.auth.application.out.UserPersistencePort;
import com.baro13.auth.domain.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserPersistencePort {
  private final UserMemoryRepository userMemoryRepository;

  @Override
  public boolean existsByUsername(String username) {
    return userMemoryRepository.existsByUsername(username);
  }

  @Override
  public User save(User user) {
    return userMemoryRepository.save(user);
  }

  @Override
  public Optional<User> findByUsername(String username) {
    return Optional.ofNullable(userMemoryRepository.findByUsername(username));
  }

  @Override
  public Optional<User> findById(Long id) {
    return Optional.ofNullable(userMemoryRepository.findById(id));
  }

}
