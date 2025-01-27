package com.example.executesqlscriptsmicroservice.chain;

public interface Process {

    void process(Context context, String sql, Integer level);
}
