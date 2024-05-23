--liquibase formatted sql
--changeset soymun:21

CREATE TABLE courses.remote_service(
    id serial PRIMARY KEY,
    name varchar(255),
    remote_url varchar(255) NOT NULL,
    remote_key varchar(255),
    remote_header varchar(255),
    create_time timestamptz,
    update_time timestamptz
);

alter table courses.remote_service
    owner to postgres;