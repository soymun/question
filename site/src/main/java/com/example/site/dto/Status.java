package com.example.site.dto;

public enum Status {

    OK(0), ERROR(1);

    final Integer p;

    Status(Integer p) {
        this.p = p;
    }
}
