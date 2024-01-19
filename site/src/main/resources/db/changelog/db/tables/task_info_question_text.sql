--liquibase formatted sql
--changeset soymun:12
create table courses.task_info_question_text
(
    id      bigserial
        primary key,
    task_id bigint
        unique
        constraint task_fk
            references courses.task,
    answer  varchar(255)
);

alter table courses.task_info_question_text
    owner to postgres;

ALTER TABLE courses.task_info_question_text add constraint unique_2 unique (task_id);

CREATE INDEX task_info_q_t_task_ind ON courses.task_info_question_text USING btree(task_id);


