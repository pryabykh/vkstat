--liquibase formatted sql

--changeset pryabykh:1
ALTER TABLE checks ADD COLUMN payload text;