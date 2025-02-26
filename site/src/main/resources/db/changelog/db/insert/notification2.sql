--liquibase formatted sql
--changeset soymun:22

ALTER TABLE notification.notification_template ADD COLUMN subject text;