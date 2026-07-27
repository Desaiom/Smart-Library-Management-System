# Smart Library Management System

A placement-ready, enterprise-architecture full-stack library system.

- **Backend:** Spring Boot 3.3 (Java 21), Spring Security + JWT, Spring Data JPA (Hibernate), MySQL (H2 for quick local runs), Lombok, ModelMapper, springdoc/OpenAPI.
- **Frontend:** React (Vite) + Bootstrap 5 + Axios (to be added).

> This project is generated as **source code you run in your own IDE** (IntelliJ + Node). It does not run inside the generation environment.

## Layout

```
smart-library/
  backend/    Spring Boot (Maven)
  frontend/   React (Vite + Bootstrap 5)   <-- pending
  docs/       README, diagrams, API docs   <-- pending
```

## Run the backend

```bash
cd smart-library/backend
./mvnw spring-boot:run        # uses the dev profile + in-memory H2 (zero setup)
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 console (dev): http://localhost:8080/h2-console  (JDBC URL `jdbc:h2:mem:smartlibrary`)

### Switch to MySQL
Edit `src/main/resources/application-dev.yml` (uncomment the MySQL block) or run with the prod profile and env vars:

```bash
SPRING_PROFILES_ACTIVE=prod DB_URL=... DB_USERNAME=... DB_PASSWORD=... JWT_SECRET=... ./mvnw spring-boot:run
```

## Build status — COMPLETE

All backend layers are implemented (90 Java classes):
- Config, Entities, Repositories, DTOs, Exceptions, Security (JWT)
- **Mappers**: BookMapper, CategoryMapper, BorrowMapper, ReviewMapper, UserMapper
- **Services + Impl**: Auth, Book, Category, Borrow, Review, User, Dashboard
- **Controllers**: Auth, Book, Category, Borrow, Review, User, Dashboard
- **AOP**: `@LogExecutionTime` + `ExecutionTimeAspect`, `LoggingAspect`
- **Seeder**: `DataSeeder` (dev profile) — admin/librarian/user demo accounts + sample books

Frontend (React + Vite + Bootstrap 5 + Axios) is complete:
- Axios instance with JWT request/response interceptors
- `AuthContext` + `ProtectedRoute` role-based routing
- Pages: Home, Login, Register, Books, BookDetails, BookForm, MyBorrows,
  ManageBorrows, Categories, Users, Dashboard, Profile, NotFound
- Shared components: Navbar, Loader, EmptyState, Pagination, RatingStars

### Run locally
Backend: `cd backend && ./mvnw spring-boot:run` (H2 dev profile, seeds demo data)
Frontend: `cd frontend && npm install && npm run dev` (proxies /api -> :8080)

Demo login: `admin@library.com / admin123`
