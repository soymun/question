--liquibase formatted sql
--changeset soymun:20

CREATE TABLE courses.reports(
    id serial PRIMARY KEY,
    name varchar(255),
    file_name text,
    sql text
)