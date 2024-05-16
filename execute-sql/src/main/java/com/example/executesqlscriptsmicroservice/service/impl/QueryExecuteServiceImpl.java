package com.example.executesqlscriptsmicroservice.service.impl;

import com.example.executesqlscriptsmicroservice.service.QueryExecuteService;
import dto.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.sql.internal.NativeQueryImpl;
import org.hibernate.transform.AliasToEntityMapResultTransformer;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class QueryExecuteServiceImpl implements QueryExecuteService {

    private final EntityManagerFactory entityManagerFactory;

    @Override
    public ResponseCheckSql checkSelectSql(RequestCheckSql requestCheckSql) {
        log.info("Check simple SELECT sql with taskUser - {}", requestCheckSql.getTaskUserId());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        setSchema(entityManager, requestCheckSql.getSchema());

        entityManager.getTransaction().begin();

        List<Map<String, Object>> userResult;

        long executeTime = 0L;

        try {
            Pair<List<Map<String, Object>>, Long> result = processSelectSql(entityManager, requestCheckSql.getUserSql());
            userResult = result.getFirst();
            executeTime = result.getSecond();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return ResponseCheckSql
                    .builder()
                    .taskUserId(requestCheckSql.getTaskUserId())
                    .status(Status.ERROR)
                    .message(e.getMessage())
                    .build();
        }

        entityManager.getTransaction().rollback();

        entityManager.getTransaction().begin();

        List<Map<String, Object>> mainResult = null;
        try {
            mainResult = processSelectSql(entityManager, requestCheckSql.getCheckSql()).getFirst();
        } catch (Exception ignored) {

        }

        entityManager.getTransaction().rollback();

        return checkAndSet(requestCheckSql.getTaskUserId(), userResult, mainResult, executeTime);
    }

    @Override
    public ResponseCheckSql checkSql(RequestCheckSql requestCheckSql) {
        log.info("Check sql with taskUser - {}", requestCheckSql.getTaskUserId());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        setSchema(entityManager, requestCheckSql.getSchema());

        entityManager.getTransaction().begin();

        List<Map<String, Object>> userResult = null;

        long executeTime = 0L;

        try {
            Date start = new Date();
            entityManager.createNativeQuery(requestCheckSql.getUserSql()).executeUpdate();
            Date end = new Date();
            executeTime = end.getTime() - start.getTime();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            return ResponseCheckSql
                    .builder()
                    .taskUserId(requestCheckSql.getTaskUserId())
                    .status(Status.ERROR)
                    .message(e.getMessage())
                    .build();
        }

        try {
            userResult = processSelectSql(entityManager, requestCheckSql.getCheckSql()).getFirst();
        } catch (Exception ignored) {

        }

        entityManager.getTransaction().rollback();

        entityManager.getTransaction().begin();

        List<Map<String, Object>> mainResult = null;

        try {
            entityManager.createNativeQuery(requestCheckSql.getMainSql()).executeUpdate();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
        }

        try {
            mainResult = processSelectSql(entityManager, requestCheckSql.getCheckSql()).getFirst();
        } catch (Exception ignored) {

        }

        entityManager.getTransaction().rollback();

        return checkAndSet(requestCheckSql.getTaskUserId(), userResult, mainResult, executeTime);
    }

    @Override
    public List<ResponseExecuteSql> executeSqlUser(RequestExecuteSql requestExecuteSql) {
        return executeSql(requestExecuteSql, false);
    }

    @Override
    public List<ResponseExecuteSql> executeSqlAdmin(RequestExecuteSql requestExecuteSql) {
        return executeSql(requestExecuteSql, true);
    }

    @Override
    public ResponseCreateSchema createSchema(RequestCreateSchema requestCreateSchema) {
        String schemaName = "course" + requestCreateSchema.getCourseId();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        String sql = "CREATE SCHEMA " + schemaName;
        try {
            entityManager.createNativeQuery(sql).executeUpdate();
        } catch (Exception ignored) {
            entityManager.getTransaction().rollback();
        }
        entityManager.getTransaction().commit();
        return ResponseCreateSchema
                .builder()
                .courseId(requestCreateSchema.getCourseId())
                .schema(schemaName)
                .build();
    }

    private List<ResponseExecuteSql> executeSql(RequestExecuteSql requestExecuteSql, Boolean admin) {
        log.info("Execute sql user - {}", requestExecuteSql.getUserId());
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        setSchema(entityManager, requestExecuteSql.getSchema());

        entityManager.getTransaction().begin();

        List<ResponseExecuteSql> responseExecuteSqls = new ArrayList<>();

        String[] sqls = requestExecuteSql.getUserSql().split(";");

        try {
            for (String sql : sqls) {
                String lowerSql = sql.toLowerCase();
                if (!lowerSql.contains("insert")
                        && !lowerSql.contains("delete")
                        && !lowerSql.contains("update")
                        && !lowerSql.contains("drop")
                        && !lowerSql.contains("create")
                        && !lowerSql.contains("alter")) {
                    Pair<List<Map<String, Object>>, Long> result = processSelectSql(entityManager, sql);
                    responseExecuteSqls.add(
                            ResponseExecuteSql.builder()
                                    .resultSelect(result.getFirst())
                                    .userId(requestExecuteSql.getUserId())
                                    .message("OK")
                                    .time(result.getSecond())
                                    .build()
                    );
                } else {
                    responseExecuteSqls.add(
                            ResponseExecuteSql.builder()
                                    .resultOther(entityManager.createNativeQuery(sql).executeUpdate())
                                    .userId(requestExecuteSql.getUserId())
                                    .message("OK")
                                    .build()
                    );
                }
            }
        } catch (Exception e) {
            responseExecuteSqls.add(
                    ResponseExecuteSql.builder()
                            .userId(requestExecuteSql.getUserId())
                            .message(e.getMessage())
                            .build()
            );
        }

        if (admin) {
            entityManager.getTransaction().commit();
        } else {
            entityManager.getTransaction().rollback();
        }

        return responseExecuteSqls;
    }

    private Pair<List<Map<String, Object>>, Long> processSelectSql(EntityManager entityManager, String sql) {
        Query query = entityManager.createNativeQuery(sql);
        NativeQueryImpl nativeQuery = (NativeQueryImpl) query;
        nativeQuery.setResultTransformer(AliasToEntityMapResultTransformer.INSTANCE);
        Date start = new Date();
        List<Map<String, Object>> result = nativeQuery.getResultList();
        Date end = new Date();
        return Pair.of(result, end.getTime() - start.getTime());
    }

    private void setSchema(EntityManager entityManager, String schema) {
        Session session = entityManager.unwrap(Session.class);
        session.doWork(connection -> {
            try {
                connection.setSchema(schema);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private ResponseCheckSql checkAndSet(Long taskUserId, List<Map<String, Object>> userResult, List<Map<String, Object>> mainResult, long time) {
        if (check(userResult, mainResult)) {
            return ResponseCheckSql
                    .builder()
                    .taskUserId(taskUserId)
                    .status(Status.OK)
                    .executeTime(time)
                    .message("Ok")
                    .build();
        } else {
            return ResponseCheckSql
                    .builder()
                    .taskUserId(taskUserId)
                    .executeTime(time)
                    .status(Status.ERROR)
                    .message("Значения отличаются")
                    .build();
        }
    }

    private boolean check(List<Map<String, Object>> user, List<Map<String, Object>> main) {
        if (user == null && main == null) return true;
        else if (user == null || main == null) return false;
        else if (user.size() != main.size()) return false;
        else if (user.size() == 0) return true;
        else {
            for (int i = 0; i < user.size(); i++) {

                List<Object> userResult = new ArrayList<>(user.get(i).values());
                List<Object> mainResult = new ArrayList<>(main.get(i).values());

                if (userResult.size() != mainResult.size()) {
                    return false;
                }
                if (!userResult.equals(mainResult)) {
                    return false;
                }
            }
        }
        return true;
    }
}
