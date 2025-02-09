--liquibase formatted sql
--changeset soymun:22

ALTER TABLE notification.notification_templat ADD COLUMN subject text;