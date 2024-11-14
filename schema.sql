CREATE TABLE users (
   id BIGSERIAL PRIMARY KEY,
   username TEXT NOT NULL UNIQUE,
   password TEXT NOT NULL,
   enabled BOOLEAN NOT NULL
);

CREATE TABLE roles (
   id BIGSERIAL PRIMARY KEY,
   name TEXT NOT NULL UNIQUE
);

CREATE TABLE user_roles (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    UNIQUE (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (role_id) REFERENCES roles(id)
);