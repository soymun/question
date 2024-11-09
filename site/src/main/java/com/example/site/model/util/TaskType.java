package com.example.site.model.util;

import lombok.Getter;

@Getter
public enum TaskType {

    NONE("NONE"), //Пустая задача просто информация
    PostgreSQL("PostgreSQL"), //Выполнение sql кода
    MySQL("MySQL"),
    QUESTION_BOX_ONE("QUESTION_BOX_ONE"), //Единственный выбор(специально для фронта)
    QUESTION_BOX_MULTI("QUESTION_BOX_MULTI"), //Множественный выбор
    QUESTION_TEXT("QUESTION_TEXT"), //Текст
    CODE("CODE"), //Код
    FILE("FILE"); //Добавление файла

    final String value;

    TaskType(String value) {
        this.value = value;
    }
}
