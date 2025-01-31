package com.example.notification.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ProtocolType {

    MAIL("MAIL"), SOCKET("SOCKET"), PUSH("PUSH");

    private final String value;
}
