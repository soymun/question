package com.example.executesqlscriptsmicroservice.chain;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DefaultCheckProcess implements Process {

    private final Integer level = 1;

    private static final List<String> patterns = List.of("from [A-Za-z0-9]+\\.[A-Za-z0-9]+", "into [A-Za-z0-9]+\\.[A-Za-z0-9]+", "drop [A-Za-z0-9]+\\.[A-Za-z0-9]+", "table [A-Za-z0-9]+\\.[A-Za-z0-9]+");

    @Override
    public void process(Context context, String sql, Integer level) {
        if (level <= this.level) {
            for (String pattern : patterns) {
                if (sql.matches(pattern)) {
                    context.setVerified(false);
                }
            }

            String lowerSql = sql.toLowerCase();

            if (lowerSql.contains("transactional")) {
                context.setVerified(false);
            }

            if (lowerSql.contains("sleep")) {
                context.setVerified(false);
            }

            if (lowerSql.contains("database")) {
                context.setVerified(false);
            }

            if (lowerSql.contains("schema")) {
                context.setVerified(false);
            }
        }
    }
}
