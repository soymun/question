--liquibase formatted sql
--changeset soymun:3

create table courses.courses
(
    course_type  smallint
        constraint courses_course_type_check
            check ((course_type >= 0) AND (course_type <= 1)),
    time_created date,
    id           bigserial
        primary key,
    user_id      bigint
        constraint user_fk
            references courses.users,
    about        varchar(255),
    course_name  varchar(255),
    path_image   varchar(255),
    open boolean default false,
    deleted boolean default false,
    schema       varchar(255)
);

alter table courses.courses
    owner to postgres;

CREATE INDEX course_user_ind ON courses.courses USING btree(user_id);
CREATE INDEX course_name_ind ON courses.courses USING btree(course_name);


