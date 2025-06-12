package com.baro13.auth.application.out;

import com.baro13.auth.domain.User;

public interface UserPersistencePort {
    boolean existsByUsername(String username);
    User findByUsername(String username);
    User save(User user);
}
