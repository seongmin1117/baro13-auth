package com.baro13.auth.application.out;

import com.baro13.auth.domain.User;
import java.util.Optional;

public interface UserPersistencePort {
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
