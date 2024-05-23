--liquibase formatted sql
--changeset soymun:4

create table courses.course_marks
(
    count_task bigint,
    course_id  bigint
        constraint course_fk
            references courses.courses,
    id         bigserial
        primary key,
    user_id bigint constraint user_fk references courses.users,
    mark       bigint,
    create_time timestamptz,
    update_time timestamptz
);

alter table courses.course_marks
    owner to postgres;

CREATE INDEX course_mark_courses_ind ON courses.course_marks USING btree(course_id);
