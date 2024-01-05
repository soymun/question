--liquibase formatted sql
--changeset soymun:9
create table courses.task_history_result
(
    rights  boolean,
    id      bigserial
        primary key,
    task_id bigint
        constraint task_fk
            references courses.task,
    user_id bigint
        constraint user_fk
            references courses.users,
    code    varchar(255),
    message varchar(255)
);

alter table courses.task_history_result
    owner to postgres;

CREATE INDEX history_result_user_ind ON courses.task_history_result USING btree(user_id);
CREATE INDEX history_result_task_ind ON courses.task_history_result USING btree(task_id);



