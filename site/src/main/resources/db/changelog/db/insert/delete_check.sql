--liquibase formatted sql
--changeset soymun:19
alter table courses.task
    drop constraint task_task_type_check;
