package com.baro13.auth.adapter.out.persistence.repository;

import com.baro13.auth.domain.User;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Repository;

@Repository
public class UserMemoryRepository {
  private final Map<Long, User> db = new ConcurrentHashMap<>();
  private final Map<String, Long> ids = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public boolean existsByUsername(final String username) {
    return ids.containsKey(username);
  }

  public User save(final User user) {
    long id = idGenerator.getAndIncrement();
    injectId(user, id);
    db.put(id, user);
    ids.put(user.getUsername(), id);
    return user;
  }

  public User findByUsername(final String username) {
    long id = ids.get(username);
    if (id < 0) {
      return null;
    }
    return db.get(id);
  }

  public User findById(final long id) {
    return db.get(id);
  }

  public void clear() {
    db.clear();
    ids.clear();
  }

  private void injectId(User user, Long id) {
    try {
      Field idField = User.class.getDeclaredField("id");
      idField.setAccessible(true);
      idField.set(user, id);
    } catch (Exception e) {
      throw new RuntimeException("ID 주입 실패", e);
    }
  }

}
