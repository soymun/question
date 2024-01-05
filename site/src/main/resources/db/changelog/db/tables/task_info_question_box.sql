--liquibase formatted sql
--changeset soymun:11

create table courses.task_info_question_box
(
    rights  boolean,
    id      bigserial
        primary key,
    task_id bigint
        constraint task_fk
            references courses.task,
    answer  varchar(255)
);

alter table courses.task_info_question_box
    owner to postgres;

CREATE INDEX task_info_q_b_task_ind ON courses.task_info_question_box USING btree(task_id);
