--liquibase formatted sql
--changeset soymun:21

create schema notification;

create table notification.notification_template
(
    id serial primary key,
    file_name         varchar(255) NOT NULL,
    notification_type smallint NOT NULL,
    protocol_type     smallint
);

