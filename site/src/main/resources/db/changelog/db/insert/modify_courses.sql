--liquibase formatted sql
--changeset soymun:16
alter table courses.courses
    add time_execute bigint;
