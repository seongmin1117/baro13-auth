## 🛡️ Baro13 Auth API

Spring Boot 기반의 인증 서버입니다.
JWT 기반 로그인, 회원가입, 관리자 권한 부여 기능을 제공합니다.

---

### ✅ 주요 링크

* **GitHub Repository:** [https://github.com/seongmin1117/baro13-auth](https://github.com/seongmin1117/baro13-auth)
* **Swagger UI:** [http://15.164.169.142:8080/swagger-ui/index.html](http://15.164.169.142:8080/swagger-ui/index.html)
* **API Endpoint:** [http://15.164.169.142:8080](http://15.164.169.142:8080)

---

## 요구사항 중 고려한 점

### 📦 1. 인메모리 저장소 설계

* **데이터베이스나 JPA 없이** 메모리 기반 저장소를 사용하여 사용자 정보를 관리합니다.
* 저장소는 멀티스레드 환경에서도 안전하게 동작해야 하므로, **`ConcurrentHashMap`** 을 사용하여 동시성 이슈를 방지했습니다.
* 사용자 ID는 자동 증가되는 값을 사용하며, **`AtomicLong`** 으로 thread-safe하게 생성합니다.
* `User` 객체의 ID 필드는 setter가 없어, JPA처럼 ID를 주입하기 위해 **Java 리플렉션(Reflection)** 을 활용합니다.

```java
Field idField = User.class.getDeclaredField("id");
idField.setAccessible(true);
idField.set(user, id);
```

* 실사용 RDBMS에서 `PRIMARY KEY`, `UNIQUE INDEX`를 관리하는 방식과 유사하게, **2개의 Map을 사용하여 식별성과 조회 성능을 확보**합니다

```java
Map<Long, User> db;         // ID → 사용자 (PK 역할)
Map<String, Long> ids;      // username → ID (UNIQUE 인덱스 역할)
```

> `username`만으로 `User`를 바로 조회할 수 있게 하기 위해 별도의 인덱스 Map(`Map<String, Long>`)을 두었으며, 이는 DB에서의 인덱싱 구조(B-Tree 기반)를 참고하였습니다.

---

### 🧩 2. 포트-어댑터 구조 적용

* 도메인 로직은 구현 기술에 의존하지 않도록, `UserPersistencePort`라는 인터페이스를 통해 저장소와 분리되었습니다.

```
[서비스] → [Port 인터페이스] → [Adapter 구현체]
AuthService → UserPersistencePort → UserPersistenceAdapter → UserMemoryRepository
```

* 현재는 메모리 기반 저장소(`UserMemoryRepository`)를 사용하지만, **JPA 기반의 `JpaUserRepository`로 쉽게 교체 가능**하도록 설계되어 있습니다.
* 실제 운영 환경에서는 `UserPersistenceAdapter`에서 주입받는 Repository만 변경하면 되므로, 유지보수성과 확장성이 뛰어납니다.

```java
public interface UserPersistencePort {
    boolean existsByUsername(String username);
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
}
```

### 🔐 API 명세 요약

#### 📝 회원가입

* **POST** `/signup`
* **Request:**

```json
{
  "username": "JIN HO",
  "password": "12341234",
  "nickname": "Mentos"
}
```

* **Response (성공):**

```json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    { "role": "USER" }
  ]
}
```

* **Response (실패):**

```json
{
  "error": {
    "code": "USER_ALREADY_EXISTS",
    "message": "이미 가입된 사용자입니다."
  }
}
```

---

#### 🔑 로그인

* **POST** `/login`
* **Request:**

```json
{
  "username": "JIN HO",
  "password": "12341234"
}
```

* **Response (성공):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR..."
}
```

* **Response (실패):**

```json
{
  "error": {
    "code": "INVALID_CREDENTIALS",
    "message": "아이디 또는 비밀번호가 올바르지 않습니다."
  }
}
```

---

#### 🛡️ 관리자 권한 부여

* **PATCH** `/admin/users/{userId}/roles`
* **Header:** `Authorization: Bearer <ACCESS_TOKEN>`
* **Response (성공):**

```json
{
  "username": "JIN HO",
  "nickname": "Mentos",
  "roles": [
    { "role": "ADMIN" }
  ]
}
```

* **Response (실패):**

```json
{
  "error": {
    "code": "ACCESS_DENIED",
    "message": "접근 권한이 없습니다."
  }
}
```

---


### 🛠 기술 스택

* Java 17
* Spring Boot 3.5
* Spring Security
* JWT (`io.jsonwebtoken:jjwt:0.11.5`)
* Swagger (SpringDoc OpenAPI)
* AWS EC2 (Ubuntu 22.04)

