--liquibase formatted sql

--changeset pryabykh:1
CREATE TABLE users (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    vk_id integer NOT NULL UNIQUE,
    version integer NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table users;

--changeset pryabykh:2
CREATE TABLE tokens (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    token text NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table tokens;

--changeset pryabykh:3
CREATE TYPE user_status AS ENUM ('Untracked', 'Tracked');
CREATE TABLE target_users (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    user_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    vk_id integer NOT NULL,
    version integer NOT NULL,
    status user_status NOT NULL,
    created_at timestamp NOT NULL,
    updated_at timestamp NOT NULL
);
--rollback drop table target_users;

--changeset pryabykh:4
CREATE TYPE activity_status AS ENUM ('Online', 'Offline');
CREATE TABLE activities (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    target_user_id bigint NOT NULL REFERENCES target_users (id) ON DELETE CASCADE,
    was_from timestamp NOT NULL,
    was_to timestamp NOT NULL,
    status activity_status NOT NULL
);
--rollback drop table activity_status;