package com.example.executesqlscriptsmicroservice.dto;

public enum Status {

    OK(0), ERROR(1);

    final Integer p;

    Status(Integer p) {
        this.p = p;
    }
}
