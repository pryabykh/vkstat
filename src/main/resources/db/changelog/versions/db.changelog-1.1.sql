--liquibase formatted sql

--changeset pryabykh:1
CREATE TYPE check_status AS ENUM ('Online', 'Offline');
CREATE TABLE checks (
    id bigint PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    owner_id bigint NOT NULL REFERENCES users (id) ON DELETE CASCADE,
    vk_id integer NOT NULL,
    status check_status NOT NULL,
    created_at timestamp NOT NULL
);