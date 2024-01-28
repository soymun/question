--liquibase formatted sql
--changeset soymun:17
alter table courses.courses
    alter column about type text using about::text;

alter table courses.courses
    alter column path_image type text using path_image::text;
