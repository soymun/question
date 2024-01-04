package com.example.executesqlscriptsmicroservice.service;


import dto.*;

import java.util.List;

public interface QueryExecuteService {

    /**
     * Выполнение SELECT запросов
     * @param requestCheckSql simple sql request
     * @return Верен или нет, запрос пользователя
     */
    ResponseCheckSql checkSelectSql(RequestCheckSql requestCheckSql);

    /**
     * Выполнение запросов, кроме SELECT
     * @param requestCheckSql запрос выполнения
     * @return Результат проверки запроса пользователя
     */
    ResponseCheckSql checkSql(RequestCheckSql requestCheckSql);

    /**
     * Выполнение запросов пользователя
     * @param requestExecuteSql запрос выполнения
     * @return Результат выполнения запросов пользователя
     */
    List<ResponseExecuteSql> executeSqlUser(RequestExecuteSql requestExecuteSql);

    /**
     * Выполнение запросов учителя/администратора
     * @param requestExecuteSql запрос выполнения
     * @return Результат выполнения запросов учителя/администратора
     */
    List<ResponseExecuteSql> executeSqlAdmin(RequestExecuteSql requestExecuteSql);

    /**
     * Создание схемы для каждого курса
     * @param requestCreateSchema запрос создания схемы
     * @return Результат создания схемы
     */
    ResponseCreateSchema createSchema(RequestCreateSchema requestCreateSchema);
}
