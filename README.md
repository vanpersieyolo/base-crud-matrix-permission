# Base Spring Boot Project

## Overview
A modular Spring Boot \(`3.5.x`\) starter template implementing JWT authentication, role / permission authorization, and context\-based (matrix) permissions with auditing, Flyway migrations, OpenAPI docs, and generic CRUD support.

## Tech Stack
\- Java 17  
\- Spring Boot (Web, Data JPA, Security, AOP, Actuator)  
\- PostgreSQL + Flyway  
\- MapStruct (DTO mapping)  
\- Lombok  
\- JJWT (token handling)  
\- Micrometer + Prometheus  
\- SpringDoc OpenAPI

## Core Modules / Packages
\- `org.example.basespringbootproject.application` \- Services, DTOs, mappers  
\- `org.example.basespringbootproject.domain.model` \- Entities (User, Role, Permission, Context, UserContextPermission)  
\- `org.example.basespringbootproject.domain.repository` \- Repositories (with `@EntityGraph` for eager auth data)  
\- `org.example.basespringbootproject.infrastructure.security` \- JWT, filter, entrypoint, permission evaluator  
\- `org.example.basespringbootproject.infrastructure.exception` \- Centralized error handling  
\- `org.example.basespringbootproject.web.controller` \- REST controllers (Auth, Admin, User, Test, Base CRUD)  
\- `src/main/resources/db/migration` \- Flyway SQL migrations (`V1__init_tables.sql`, `V2__add_sample_data.sql`)

## Database & Migrations
\- Flyway auto runs on startup.  
\- `V1__init_tables.sql` creates base tables (users, roles, permissions, contexts, join/matrix tables).  
\- `V2__add_sample_data.sql` seeds sample users, roles, permissions, mappings.  
\- Passwords use BCrypt; sample hash for literal `password`.

## Security Model
\- Stateless JWT \- issued on `/auth/login` and `/auth/register`.  
\- Token parsed by `JwtAuthenticationFilter`; username loaded via `CustomUserDetailsService`.  
\- Authorities built eagerly from roles and their permissions:  
\- Roles exposed as `ROLE_<CODE>`  
\- Permissions exposed as `PERM_<CODE>`  
\- Method security via `@PreAuthorize`.  
\- Context (matrix) permissions resolved dynamically by `CustomPermissionEvaluator` (e.g. `@PreAuthorize("@permissionEvaluator.hasPermission(authentication, 'USER_UPDATE', #contextId)")`).  
\- Public endpoints: `/auth/**`, actuator, Swagger UI.  
\- All other paths require authentication.

## Endpoints (Representative)
\- `/auth/login` \- POST \- returns JWT.  
\- `/auth/register` \- POST \- create user + returns JWT.  
\- `/auth/refresh` \- POST \- new token.  
\- `/users/me` \- GET \- current user DTO.  
\- `/admin/roles`, `/admin/permissions`, `/admin/users/{id}/roles` \- secured by role/permission.  
\- `/test/*` \- demo protected and permission/matrix checks.  
\- Generic CRUD endpoints provided by controllers extending `AbstractBaseController`.

## Error Handling
\- Centralized in `GlobalExceptionHandler`.  
\- Structured `ApiError` includes HTTP status, code (`ErrorCode` enum), message, field validation errors.  
\- 401 on invalid credentials (`BadCredentialsException`).  
\- 400 on validation / bad arguments.  
\- 403 on forbidden access (Spring Security).

## Auditing
\- Enabled by `@EnableJpaAuditing` in `PersistenceConfiguration`.  
\- Auditor resolved from `SecurityContextHolder` or falls back to `system`.

## Token Details
\- HS256 signing using secret from properties (`security.jwt.secret`).  
\- Expiration configured via `security.jwt.expiration-ms`.  
\- Subject = username only (extend with claims if needed).

## Build & Run
\-\- Prerequisites: Java 17, Maven, PostgreSQL running with database matching Flyway config.  
\-\- Steps:
1. Configure application `application.yml` (datasource, JWT secret, expiration).
2. Run `mvn spring-boot:run` or start `BaseSpringbootProjectApplication`.
3. Access Swagger UI at `/swagger-ui.html` or `/swagger-ui/index.html`.
4. Authenticate: POST `/auth/login` with JSON `{ "username": "admin1", "password": "password" }` then use `Authorization: Bearer <token>`.

## Testing Permissions
\-\- Role based: `@PreAuthorize("hasAuthority('ROLE_ADMIN')")`  
\-\- Permission based: `@PreAuthorize("hasAuthority('PERM_USER_READ')")`  
\-\- Context based: `@PreAuthorize("@permissionEvaluator.hasPermission(authentication, 'USER_UPDATE', #contextId)")`

## Extending
\-\- Add new entity: create model, repository, mapper, service, controller extending `AbstractBaseController`.  
\-\- Add permission: insert into `permissions` table and assign to roles or matrix.  
\-\- Add claim data to JWT: modify `JwtTokenProvider.createToken`.  
\-\- Customize error codes: extend `ErrorCode` enum and handler.

## Security Notes
\-\- Avoid switching collections to `FetchType.EAGER` globally; use `@EntityGraph`.  
\-\- Always hash passwords with `BCryptPasswordEncoder`.  
\-\- Regenerate secret for production and keep out of source control.

## Metrics & Actuator
\-\- `/actuator/health`, `/actuator/prometheus` available (public if permitted).

## License
\-\- Customize license section as needed.# base-crud-matrix-permission
