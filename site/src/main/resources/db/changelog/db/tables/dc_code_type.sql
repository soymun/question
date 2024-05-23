--liquibase formatted sql
--changeset soymun:19
create table courses.dc_code_type(
    id serial PRIMARY KEY,
    name varchar(255)
);

alter table courses.dc_code_type
    owner to postgres;

CREATE INDEX dc_code_type_name_ind ON courses.dc_code_type USING btree(name);