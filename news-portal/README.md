# News Portal — Spring Boot + Thymeleaf + MySQL + Spring Security (JWT)

A multi-category news website: browse categories → see headlines → read full
article → post comments (auth required). Built for a Java practical test.

## Tech stack
- Java 17, Spring Boot 3.3
- Spring MVC + Thymeleaf (server-rendered pages)
- Spring Data JPA + MySQL
- Spring Security with **JWT stored in an httpOnly cookie** (not
  localStorage) — this lets stateless JWT auth work transparently with
  server-rendered Thymeleaf forms, no JS needed
- ModelMapper for entity↔DTO conversion
- Lombok
- CommandLineRunner-based `DataInitializer` that seeds roles, one demo user
  per role, categories, and sample news on first startup

## Project structure
```
com.newsportal
 ├─ config          SecurityConfig, ModelMapperConfig, DataInitializer
 ├─ controller       AuthController, CategoryController, NewsController,
 │                    EditorController, AdminController
 ├─ dto              request/response DTOs
 ├─ entity           User, Role, Category, News, Comment (JPA entities)
 ├─ exception        custom exceptions + @ControllerAdvice
 ├─ repository       Spring Data JPA repositories
 ├─ security         JwtUtil, JwtAuthFilter, CustomUserDetails(Service)
 └─ service / impl   business logic (interface + impl per convention)
```

## Roles
| Role   | Can do |
|--------|--------|
| READER | browse, read news, post comments (self-registration defaults to this role) |
| EDITOR | everything READER can, plus create/edit/delete news (`/editor/**`) |
| ADMIN  | everything EDITOR can, plus manage categories (`/admin/**`) |

## Demo accounts (seeded automatically on first run)
| Username | Password   | Role   |
|----------|-----------|--------|
| admin    | Admin@123 | ADMIN  |
| editor   | Editor@123| EDITOR |
| reader   | Reader@123| READER |

## Setup

1. **Create the database** (or let `createDatabaseIfNotExist=true` in the
   JDBC URL handle it — just make sure the MySQL user has permission):
   ```sql
   CREATE DATABASE newsdb;
   ```

2. **Configure credentials** in
   `src/main/resources/application.properties`:
   ```properties
   spring.datasource.username=root
   spring.datasource.password=yourpassword
   ```
   Also change `app.jwt.secret` to your own long random string before
   using this anywhere beyond the interview/demo.

3. **Run it**:
   ```bash
   mvn spring-boot:run
   ```
   or build a jar:
   ```bash
   mvn clean package
   java -jar target/news-portal.jar
   ```

4. Visit `http://localhost:8080/`.

> Note: this project was generated in a sandboxed environment without
> access to Maven Central, so `mvn compile` could not be run here to
> verify the build. Please do a build check locally before your
> interview/demo — if anything doesn't compile, it's most likely a minor
> import or version mismatch that's quick to fix.

## How the JWT + Thymeleaf combo works
Most JWT tutorials assume a stateless REST API with a JS frontend storing
the token in `localStorage`. That doesn't fit a server-rendered Thymeleaf
app well (no JS required, no localStorage access on the server). Instead:

1. `POST /perform_login` authenticates via `AuthenticationManager`, then
   generates a JWT and sets it as an **httpOnly** cookie.
2. `JwtAuthFilter` (a `OncePerRequestFilter`) runs on every request, reads
   the JWT from that cookie, validates it, and populates the
   `SecurityContext` — so Spring Security still sees a fully authenticated
   user, but no server-side session/state is kept (`SessionCreationPolicy.STATELESS`).
3. Thymeleaf's `sec:authorize` / `sec:authentication` tags (via
   `thymeleaf-extras-springsecurity6`) then work exactly as they would with
   traditional session-based auth.

## Extending it
- Add pagination to `/editor/news` and category listings for large datasets
- Add a "my comments" page per user
- Add refresh tokens if you want shorter-lived access tokens
- Swap `ddl-auto=update` for Flyway/Liquibase migrations for production use
