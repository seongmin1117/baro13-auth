package com.baro13.auth.adapter.out.repository;

import com.baro13.auth.domain.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Repository;

@Repository
public class UserMemoryRepository {
  private final Map<String, User> db = new ConcurrentHashMap<>();

  public boolean existsByUsername(final String username) {
    return db.containsKey(username);
  }

  public User save(final User user) {
    db.put(user.getUsername(), user);
    return user;
  }

  public User findByUsername(final String username) {
    return db.get(username);
  }

  public void clear() {
    db.clear();
  }
}
