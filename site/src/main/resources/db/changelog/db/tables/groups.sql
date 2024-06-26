--liquibase formatted sql
--changeset soymun:1

create table courses.groups
(
    id         bigserial
        primary key,
    full_name  varchar(255),
    short_name varchar(255),
    create_time timestamptz,
    update_time timestamptz
);

alter table courses.groups
    owner to postgres;

