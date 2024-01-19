--liquibase formatted sql
--changeset soymun:10
create table courses.task_info_code
(
    code_type   smallint
        constraint task_info_code_code_type_check
            check ((code_type >= 0) AND (code_type <= 1)),
    id          bigserial
        primary key,
    task_id     bigint
        constraint task_fk
            references courses.task,
    check_class varchar(255),
    check_code  varchar(255),
    init_code   varchar(255),
    user_class  varchar(255)
);

alter table courses.task_info_code
    owner to postgres;

ALTER TABLE courses.task_info_code add constraint unique_1 unique (task_id, code_type);

CREATE INDEX task_info_c_task_ind ON courses.task_info_code USING btree(task_id);


