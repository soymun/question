--liquibase formatted sql
--changeset soymun:20

CREATE TABLE courses.reports(
    id serial PRIMARY KEY,
    name varchar(255),
    file_name text,
    permission bigint,
    default_report boolean,
    sql text
);

alter table courses.reports
    owner to postgres;

CREATE INDEX filename_ind ON courses.reports USING btree(file_name);