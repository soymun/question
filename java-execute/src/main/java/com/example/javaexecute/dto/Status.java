package com.example.javaexecute.dto;

public enum Status {

    OK(0), ERROR(1);

    final Integer p;

    Status(Integer p) {
        this.p = p;
    }
}
