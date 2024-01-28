--liquibase formatted sql
--changeset soymun:15
alter table courses.comments
    add apply boolean;