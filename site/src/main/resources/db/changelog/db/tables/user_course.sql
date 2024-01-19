--liquibase formatted sql
--changeset soymun:8

create table courses.user_course
(
    closed     boolean,
    end_date   timestamp,
    start_date timestamp,
    deleted     boolean default false,
    course_id  bigint not null
        constraint course_fk
            references courses.courses,
    mark_id    bigint
        constraint mark_fk
            references courses.course_marks,
    user_id    bigint not null
        constraint user_fk
            references courses.users,
    primary key (course_id, user_id)
);

alter table courses.user_course
    owner to postgres;

CREATE INDEX user_course_user_ind ON courses.user_course USING btree(user_id);
CREATE INDEX user_course_course_ind ON courses.user_course USING btree(course_id);
