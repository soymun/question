--liquibase formatted sql
--changeset soymun:21

CREATE TABLE courses.remote_service(
    id serial PRIMARY KEY,
    name varchar(255),
    remote_key varchar(255),
    remote_header varchar(255)
)