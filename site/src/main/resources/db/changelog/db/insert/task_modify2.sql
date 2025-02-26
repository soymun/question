--liquibase formatted sql
--changeset soymun:22

ALTER table courses.task ADD COLUMN file varchar(255);
ALTER table courses.task ADD COLUMN task_group_id integer;
ALTER table courses.task ADD CONSTRAINT task_group_fk FOREIGN KEY (task_group_id) REFERENCES courses.task_group