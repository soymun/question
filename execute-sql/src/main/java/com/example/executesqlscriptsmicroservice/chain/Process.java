package com.example.executesqlscriptsmicroservice.chain;

import java.util.Map;

public interface Process {

    void process(Context context, String sql, Integer level);
}
