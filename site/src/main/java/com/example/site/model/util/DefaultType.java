package com.example.site.model.util;

public enum DefaultType {

    LONG("LONG"),
    STRING("STRING"),
    UUID("UUID"),
    LOCALDATETIME("LOCALDATETIME"),
    LOCALDATE("LOCALDATE");

    private String value;

    DefaultType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
