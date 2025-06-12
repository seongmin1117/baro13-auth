package com.baro13.auth.adapter.out.persistence;

import com.baro13.auth.adapter.out.persistence.repository.UserMemoryRepository;
import com.baro13.auth.application.out.UserPersistencePort;
import com.baro13.auth.domain.User;
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
  public User findByUsername(String username) {
    return userMemoryRepository.findByUsername(username);
  }


}
