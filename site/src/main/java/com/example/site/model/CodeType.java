package com.example.site.model;

public enum CodeType {

    JAVA("JAVA"), PYTHON("PYTHON"), CR("C#"), KOTLIN("KOTLIN"), C("C"), CPLUS("C++");

    final String value;

    CodeType(String value) {
        this.value = value;
    }
}
