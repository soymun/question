--liquibase formatted sql
--changeset soymun:20
create table courses.dc_report_param_type(
    id serial PRIMARY KEY,
    name varchar(255),
    default_type bigint
);

alter table courses.dc_report_param_type
    owner to postgres;

CREATE INDEX dc_report_param_type_name_ind ON courses.dc_report_param_type USING btree(name);