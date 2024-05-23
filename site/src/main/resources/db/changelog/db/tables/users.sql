--liquibase formatted sql
--changeset soymun:2

create table courses.users
(
    active      boolean,
    role        smallint
        constraint users_role_check
            check ((role >= 0) AND (role <= 2)),
    first_entry timestamp(6),
    group_id    bigint
        constraint group_fk
            references courses.groups,
    id          bigserial
        primary key,
    last_entry  timestamp(6),
    email       varchar(255),
    first_name  varchar(255),
    password    varchar(255),
    patronymic  varchar(255),
    second_name varchar(255),
    create_time timestamptz,
    update_time timestamptz
);

alter table courses.users
    owner to postgres;

CREATE INDEX user_email_ind ON courses.users USING btree(email);
CREATE INDEX user_group_ind ON courses.users USING btree(group_id);