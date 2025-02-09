package com.example.site.dto.notification;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum NotificationType {

    PASSWORD("PASSWORD"), ADD_TO_COURSES("ADD_TO_COURSES"), CONFIRM_TASK("CONFIRM_TASK"), REGISTRATION("REGISTRATION");

    private final String value;

}
