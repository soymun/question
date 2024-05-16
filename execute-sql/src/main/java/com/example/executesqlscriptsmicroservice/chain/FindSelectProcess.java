package com.example.executesqlscriptsmicroservice.chain;

import org.springframework.stereotype.Component;

@Component
public class FindSelectProcess implements Process {

    private final Integer level = 2;
    @Override
    public void process(Context context, String sql, Integer level) {
        if(level <= this.level) {
            context.setSelected(sql.contains("insert")
                    || sql.contains("delete")
                    || sql.contains("update")
                    || sql.contains("drop")
                    || sql.contains("create")
                    || sql.contains("alter"));
        }
    }
}
