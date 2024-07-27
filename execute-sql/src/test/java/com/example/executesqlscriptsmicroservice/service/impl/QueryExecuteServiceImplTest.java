package com.example.executesqlscriptsmicroservice.service.impl;

import com.example.executesqlscriptsmicroservice.dto.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class QueryExecuteServiceImplTest {


    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    );

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @Autowired
    QueryExecuteServiceImpl queryExecuteService;

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSelectSqlTrue() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSelectSql(RequestCheckSql
                .builder()
                .schema("course1")
                .userSql("SELECT id FROM groups")
                .checkSql("SELECT id FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.OK);
        assertNotNull(responseCheckSql.getExecuteTime());
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSelectSqlFalse() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSelectSql(RequestCheckSql
                .builder()
                .schema("course1")
                .userSql("SELECT id FROM groups")
                .checkSql("SELECT name FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.ERROR);
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSelectSqlAnotherSchema() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSelectSql(RequestCheckSql
                .builder()
                .schema("course2")
                .userSql("SELECT id FROM groups")
                .mainSql("SELECT name FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.ERROR);
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSqlTrue() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSql(RequestCheckSql
                .builder()
                .schema("course1")
                .userSql("INSERT INTO groups VALUES(3, 'test3')")
                .mainSql("INSERT INTO groups VALUES(3, 'test3')")
                .checkSql("SELECT name FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.OK);
        assertNotNull(responseCheckSql.getExecuteTime());
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSqlFalse() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSql(RequestCheckSql
                .builder()
                .schema("course1")
                .userSql("INSERT INTO groups VALUES(4, 'test3')")
                .mainSql("INSERT INTO groups VALUES(3, 'test3')")
                .checkSql("SELECT id FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.ERROR);
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void checkSqlAnotherSchema() {
        ResponseCheckSql responseCheckSql = queryExecuteService.checkSql(RequestCheckSql
                .builder()
                .schema("course2")
                .userSql("INSERT INTO groups VALUES(3, 'test3')")
                .mainSql("INSERT INTO groups VALUES(3, 'test3')")
                .checkSql("SELECT name FROM groups")
                .taskUserId(1L)
                .build());

        assertEquals(responseCheckSql.getStatus(), Status.ERROR);
    }

    @Test
    @Sql(scripts = {"/drop.sql"})
    void createSchema() {
        assertEquals(queryExecuteService.createSchema(RequestCreateSchema.builder().courseId(1L).build()).getSchema(), "course1");
    }

    @Test
    @Sql(scripts = {"/drop.sql", "/create.sql"})
    void executeSqlUser() {
        List<ResponseExecuteSql> responseExecuteSql =
                queryExecuteService.executeSqlUser(RequestExecuteSql
                        .builder()
                        .admin(true)
                        .schema("course1")
                        .userId(1L)
                        .userSql("INSERT INTO groups VALUES(3, 'test3')")
                        .build());

        assertEquals(responseExecuteSql.size(), 1);
        assertNotNull(responseExecuteSql.get(0).getResultOther());

        List<ResponseExecuteSql> responseExecuteSqlSelect =
                queryExecuteService.executeSqlUser(RequestExecuteSql
                        .builder()
                        .admin(false)
                        .schema("course1")
                        .userId(1L)
                        .userSql("SELECT name FROM groups")
                        .build());

        assertEquals(responseExecuteSqlSelect.size(), 1);
        assertEquals(responseExecuteSqlSelect.get(0).getResultSelect().size(), 2);
    }
}