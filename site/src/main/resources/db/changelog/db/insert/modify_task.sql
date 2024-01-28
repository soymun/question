--liquibase formatted sql
--changeset soymun:18
alter table courses.task
    alter column description type text using description::text;

alter table courses.task
    add deleted boolean;

alter table courses.task
    add open boolean;
