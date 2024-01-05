--liquibase formatted sql
--changeset soymun:13

create table courses.task_info_sql
(
    id        bigserial
        primary key,
    task_id   bigint
        unique
        constraint task_fk
            references courses.task,
    check_sql varchar(255),
    main_sql  varchar(255)
);

alter table courses.task_info_sql
    owner to postgres;

CREATE INDEX task_info_sql_task_ind ON courses.task_info_sql USING btree(task_id);


