--liquibase formatted sql
--changeset soymun:6

create table courses.comments
(
    id      bigserial
        primary key,
    task_id bigint
        constraint task_fk
            references courses.task,
    time    timestamp(6),
    user_id bigint
        constraint user_fk
            references courses.users,
    message varchar(255)
);

alter table courses.comments
    owner to postgres;

CREATE INDEX comments_task_ind ON courses.comments USING btree(task_id);
