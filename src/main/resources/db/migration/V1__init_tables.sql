-- ==========================================
--  Flyway Migration: V1__init_table.sql
--  Purpose: Initialize core tables for
--           user-role-permission matrix auth
-- ==========================================

-- =====================
-- USERS
-- =====================
CREATE TABLE users
(
    id                 BIGSERIAL PRIMARY KEY,
    user_name           VARCHAR(100) NOT NULL UNIQUE,
    password           VARCHAR(255) NOT NULL,
    full_name          VARCHAR(255),
    is_active          BOOLEAN      NOT NULL DEFAULT TRUE,
    email              VARCHAR(255) UNIQUE,

    created_by         VARCHAR(100),
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    version            BIGINT       NOT NULL DEFAULT 0
);

-- =====================
-- ROLES
-- =====================
CREATE TABLE roles
(
    id                 BIGSERIAL PRIMARY KEY,
    code               VARCHAR(100) NOT NULL UNIQUE,
    name               VARCHAR(255) NOT NULL,

    created_by         VARCHAR(100),
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    version            BIGINT       NOT NULL DEFAULT 0
);

-- =====================
-- PERMISSIONS
-- =====================
CREATE TABLE permissions
(
    id                 BIGSERIAL PRIMARY KEY,
    code               VARCHAR(100) NOT NULL UNIQUE,
    description        VARCHAR(255),

    created_by         VARCHAR(100),
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    version            BIGINT       NOT NULL DEFAULT 0
);

-- =====================
-- CONTEXTS (optional for matrix)
-- =====================
CREATE TABLE contexts
(
    id                 BIGSERIAL PRIMARY KEY,
    context_type       VARCHAR(100) NOT NULL,
    context_value      VARCHAR(100) NOT NULL,

    created_by         VARCHAR(100),
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    is_deleted         BOOLEAN      NOT NULL DEFAULT FALSE,
    version            BIGINT       NOT NULL DEFAULT 0,

    CONSTRAINT uq_context_type_value UNIQUE (context_type, context_value)
);

-- =====================
-- USER_ROLES (many-to-many)
-- =====================
CREATE TABLE user_roles
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_user_roles_role FOREIGN KEY (role_id) REFERENCES roles (id)
);

-- =====================
-- ROLE_PERMISSIONS (many-to-many)
-- =====================
CREATE TABLE role_permissions
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    PRIMARY KEY (role_id, permission_id),
    CONSTRAINT fk_role_permissions_role FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_role_permissions_permission FOREIGN KEY (permission_id) REFERENCES permissions (id)
);

-- =====================
-- USER_CONTEXT_PERMISSIONS (matrix)
-- =====================
CREATE TABLE user_context_permissions
(
    id                 BIGSERIAL PRIMARY KEY,
    user_id            BIGINT  NOT NULL,
    context_id         BIGINT  NOT NULL,
    permission_id      BIGINT  NOT NULL,

    created_by         VARCHAR(100),
    created_date       TIMESTAMP WITH TIME ZONE,
    last_modified_by   VARCHAR(100),
    last_modified_date TIMESTAMP WITH TIME ZONE,
    is_deleted         BOOLEAN NOT NULL DEFAULT FALSE,
    version            BIGINT  NOT NULL DEFAULT 0,

    CONSTRAINT fk_ucp_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_ucp_context FOREIGN KEY (context_id) REFERENCES contexts (id),
    CONSTRAINT fk_ucp_permission FOREIGN KEY (permission_id) REFERENCES permissions (id),
    CONSTRAINT uq_user_context_permission UNIQUE (user_id, context_id, permission_id)
);

-- =====================
-- INDEXES
-- =====================
CREATE INDEX idx_users_username ON users (user_name);
CREATE INDEX idx_roles_code ON roles (code);
CREATE INDEX idx_permissions_code ON permissions (code);
CREATE INDEX idx_contexts_type_value ON contexts (context_type, context_value);

-- =====================
-- DEFAULT DATA (optional)
-- =====================
INSERT INTO roles (code, name, created_by, created_date)
VALUES ('ADMIN', 'Administrator', 'system', NOW()),
       ('USER', 'Standard User', 'system', NOW());

INSERT INTO permissions (code, description, created_by, created_date)
VALUES ('USER_VIEW', 'View user info', 'system', NOW()),
       ('USER_EDIT', 'Edit user info', 'system', NOW()),
       ('ROLE_MANAGE', 'Manage roles', 'system', NOW()),
       ('PERMISSION_MANAGE', 'Manage permissions', 'system', NOW());
