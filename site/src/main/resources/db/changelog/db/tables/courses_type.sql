--liquibase formatted sql
--changeset soymun:19

alter table courses.courses
    add sql_type smallint;


