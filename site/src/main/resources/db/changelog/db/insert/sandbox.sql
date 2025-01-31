--liquibase formatted sql
--changeset soymun:19

CREATE schema sandbox;

CREATE TABLE courses.sandbox (
    id serial PRIMARY KEY,
    open boolean default false,
    schema_name varchar(255) not null,
    sql_clear text
);

INSERT INTO courses.sandbox(open, schema_name, sql_clear) values (true, 'sandbox', 'DROP SCHEMA sandbox; Create SCHEMA sandbox; CREATE TABLE test(id serial);')