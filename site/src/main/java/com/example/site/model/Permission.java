package com.example.site.model;

public enum Permission {

    USER("USER"), TEACHER("TEACHER"), ADMIN("ADMIN");

    final String value;

    Permission(String value) {
        this.value = value;
    }
}
