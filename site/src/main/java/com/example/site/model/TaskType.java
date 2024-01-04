package com.example.site.model;

public enum TaskType {

    NONE("NONE"), SQL("SQL"),
    QUESTION_BOX_ONE("QUESTION_BOX_ONE"), QUESTION_BOX_MULTI("QUESTION_BOX_MULTI"),
    QUESTION_TEXT("QUESTION_TEXT"), CODE("CODE");

    final String value;

    TaskType(String value) {
        this.value = value;
    }
}
