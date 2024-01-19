--liquibase formatted sql
--changeset soymun:7

create table courses.user_task
(
    closed  boolean,
    rights  boolean,
    attempt bigint default 1,
    task_id bigint not null
        constraint task_fk
            references courses.task,
    user_id bigint not null
        constraint user_fk
            references courses.users,
    primary key (task_id, user_id)
);

alter table courses.user_task
    owner to postgres;

CREATE INDEX user_task_user_ind ON courses.user_task USING btree(user_id);
