--liquibase formatted sql
--changeset soymun:14
INSERT INTO courses.users(active, role, first_entry, group_id, last_entry, email, first_name, password, patronymic, second_name) VALUES (true, 2, null, null , null, 'admin', 'admin', '$2a$12$A/fInR3kiPPwS2LPb.fzp.yCOIHN42uZAeGbbE9wDXvqYDb5m3NUS', 'admin', 'admin');