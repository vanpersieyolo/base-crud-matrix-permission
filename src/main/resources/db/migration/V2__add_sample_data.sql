-- ==========================================
--  Flyway Migration: V2__sample_data.sql
--  Purpose: Seed sample users, roles, permissions
--           and matrix-based access control data
-- ==========================================

-- BCrypt for password "password"
-- $2a$10$7EqJtq98hPqEX7fNZaFWoO5j8S8Q9Z3
--
-- rrodpAtxEvsC0BpFkacwZa

-- =====================
-- USERS
-- =====================
INSERT INTO users (user_name, password, full_name, is_active, email, created_by, created_date)
VALUES ('admin1', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5j8S8Q9Z3rrodpAtxEvsC0BpFkacwZa', 'Admin One', TRUE,
        'admin1@example.com', 'seed', NOW()),
       ('admin2', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5j8S8Q9Z3rrodpAtxEvsC0BpFkacwZa', 'Admin Two', TRUE,
        'admin2@example.com', 'seed', NOW()),
       ('john', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5j8S8Q9Z3rrodpAtxEvsC0BpFkacwZa', 'John Doe', TRUE, 'john@example.com',
        'seed', NOW()),
       ('jane', '$2a$10$7EqJtq98hPqEX7fNZaFWoO5j8S8Q9Z3rrodpAtxEvsC0BpFkacwZa', 'Jane Roe', TRUE, 'jane@example.com',
        'seed', NOW())
ON CONFLICT (user_name) DO NOTHING;

-- =====================
-- PERMISSIONS (CRUD model)
-- =====================
INSERT INTO permissions (code, description, created_by, created_date)
VALUES
    -- USER CRUD
    ('USER_CREATE', 'Create user', 'seed', NOW()),
    ('USER_READ', 'View user', 'seed', NOW()),
    ('USER_UPDATE', 'Update user', 'seed', NOW()),
    ('USER_DELETE', 'Delete user', 'seed', NOW()),

    -- ROLE CRUD
    ('ROLE_CREATE', 'Create role', 'seed', NOW()),
    ('ROLE_READ', 'View role', 'seed', NOW()),
    ('ROLE_UPDATE', 'Update role', 'seed', NOW()),
    ('ROLE_DELETE', 'Delete role', 'seed', NOW()),

    -- PERMISSION manage
    ('PERMISSION_MANAGE', 'Manage permissions', 'seed', NOW())
ON CONFLICT (code) DO NOTHING;

-- =====================
-- ROLES
-- =====================
INSERT INTO roles (code, name, created_by, created_date)
VALUES ('ADMIN', 'Administrator', 'seed', NOW()),
       ('MANAGER', 'Manager', 'seed', NOW()),
       ('STAFF', 'Staff', 'seed', NOW())
ON CONFLICT (code) DO NOTHING;

-- =====================
-- ROLE → PERMISSIONS
-- =====================

-- ADMIN: full CRUD on everything
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p ON TRUE
WHERE r.code = 'ADMIN'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- MANAGER: only READ + UPDATE user/role
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON (p.code IN ('USER_READ', 'USER_UPDATE', 'ROLE_READ', 'ROLE_UPDATE'))
WHERE r.code = 'MANAGER'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- STAFF: only READ user
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.code IN ('USER_READ')
WHERE r.code = 'STAFF'
ON CONFLICT (role_id, permission_id) DO NOTHING;

-- =====================
-- USER → ROLE
-- =====================

-- admin1 → ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'ADMIN'
WHERE u.user_name = 'admin1'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- admin2 → ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'ADMIN'
WHERE u.user_name = 'admin2'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- john → MANAGER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'MANAGER'
WHERE u.user_name = 'john'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- jane → STAFF
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM users u
         JOIN roles r ON r.code = 'STAFF'
WHERE u.user_name = 'jane'
ON CONFLICT (user_id, role_id) DO NOTHING;

-- =====================
-- CONTEXTS
-- =====================
INSERT INTO contexts (context_type, context_value, created_by, created_date)
VALUES ('PROJECT', 'P1', 'seed', NOW()),
       ('PROJECT', 'P2', 'seed', NOW()),
       ('DEPARTMENT', 'D1', 'seed', NOW())
ON CONFLICT (context_type, context_value) DO NOTHING;

-- =====================
-- USER → CONTEXT → PERMISSION (override)
-- =====================

-- admin1 chỉ được READ user trong tất cả project
INSERT INTO user_context_permissions (user_id, context_id, permission_id, created_by, created_date)
SELECT u.id, c.id, p.id, 'seed', NOW()
FROM users u
         JOIN contexts c ON c.context_type = 'PROJECT'
         JOIN permissions p ON p.code = 'USER_READ'
WHERE u.user_name = 'admin1'
ON CONFLICT
    ON CONSTRAINT uq_user_context_permission DO NOTHING;

-- admin2 có toàn quyền USER CRUD trong tất cả project
INSERT INTO user_context_permissions (user_id, context_id, permission_id, created_by, created_date)
SELECT u.id, c.id, p.id, 'seed', NOW()
FROM users u
         JOIN contexts c ON c.context_type = 'PROJECT'
         JOIN permissions p ON p.code IN ('USER_CREATE', 'USER_READ', 'USER_UPDATE', 'USER_DELETE')
WHERE u.user_name = 'admin2'
ON CONFLICT
    ON CONSTRAINT uq_user_context_permission DO NOTHING;

-- john chỉ có USER_UPDATE trong Project P1
INSERT INTO user_context_permissions (user_id, context_id, permission_id, created_by, created_date)
SELECT u.id, c.id, p.id, 'seed', NOW()
FROM users u
         JOIN contexts c ON c.context_type = 'PROJECT' AND c.context_value = 'P1'
         JOIN permissions p ON p.code = 'USER_UPDATE'
WHERE u.user_name = 'john'
ON CONFLICT
    ON CONSTRAINT uq_user_context_permission DO NOTHING;

-- jane chỉ có USER_READ trong Project P2
INSERT INTO user_context_permissions (user_id, context_id, permission_id, created_by, created_date)
SELECT u.id, c.id, p.id, 'seed', NOW()
FROM users u
         JOIN contexts c ON c.context_type = 'PROJECT' AND c.context_value = 'P2'
         JOIN permissions p ON p.code = 'USER_READ'
WHERE u.user_name = 'jane'
ON CONFLICT
    ON CONSTRAINT uq_user_context_permission DO NOTHING;

-- =====================
-- DONE
-- =====================
