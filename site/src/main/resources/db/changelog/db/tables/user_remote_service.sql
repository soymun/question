--liquibase formatted sql
--changeset soymun:21
CREATE TABLE courses.user_remote_service(
    id serial PRIMARY KEY,
    user_id bigint constraint user_fk references courses.users,
    remote_service_id bigint constraint remote_fk  references courses.remote_service,
    create_time timestamptz,
    update_time timestamptz
);

alter table courses.user_remote_service
    owner to postgres;

CREATE INDEX user_remote_service_user_id_ind ON courses.user_remote_service USING btree(user_id);
CREATE INDEX user_remote_service_remote_service_id_ind ON courses.user_remote_service USING btree(remote_service_id);