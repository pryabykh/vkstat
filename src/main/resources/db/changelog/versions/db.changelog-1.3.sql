--liquibase formatted sql

--changeset pryabykh:1
CREATE CAST (varchar AS check_status) WITH INOUT AS IMPLICIT;