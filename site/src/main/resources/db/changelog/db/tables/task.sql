--liquibase formatted sql
--changeset soymun:5

create table courses.task
(
    task_type     smallint
        constraint task_task_type_check
            check ((task_type >= 0) AND (task_type <= 5)),
    course_id     bigint
        constraint course_fk
            references courses.courses,
    id            bigserial
        primary key,
    number        bigint,
    description   varchar(255),
    name          varchar(255),
    title         varchar(255)
);

alter table courses.task
    owner to postgres;

CREATE INDEX task_courses_ind ON courses.task USING btree(course_id);
CREATE INDEX task_number_ind ON courses.task USING btree(number);

