package com.example.executesqlscriptsmicroservice.chain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UtilProcess {

    @Autowired
    private List<Process> processes;

    public Context process(String sql, Integer level){

        Context context = new Context();

        String lowerSql = sql.toLowerCase();

        for (Process process: processes){
            process.process(context, lowerSql, level);
        }

        return context;
    }
}
