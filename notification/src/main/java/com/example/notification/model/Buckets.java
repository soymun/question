package com.example.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Buckets {

    NOTIFICATION("notification");

    private final String value;
}
