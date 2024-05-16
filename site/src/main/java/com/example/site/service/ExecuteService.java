package com.example.site.service;

import com.example.site.dto.task.*;

public interface ExecuteService {

    void executeSql(ExecuteSqlDto executeSqlDto, Long id);

    ResultExecute executeText(ExecuteTextDto executeBoxDto, Long id);

    ResultExecute executeBox(ExecuteBoxDto executeBoxDto, Long id);

    void executeCode(ExecuteCodeDto executeCodeDto, Long id);
}
