--liquibase formatted sql
--changeset soymun:23

CREATE TABLE courses.task_group (
    id serial PRIMARY KEY,
    name varchar(255),
    number int,
    course_id int constraint course_fk references courses.courses,
    delete boolean
)