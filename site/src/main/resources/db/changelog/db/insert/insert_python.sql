--liquibase formatted sql
--changeset soymun:20
INSERT INTO courses.dc_code_type(name) VALUES ('PYTHON');
