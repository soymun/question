package com.example.site.dto.course;

import lombok.Data;

@Data
public class CourseRequestDto {

    private String query;

    private Boolean teacher;


    public String getQuery() {
        if (query == null) {
            return "";
        } else {
            return "%" + query.toLowerCase() + "%";
        }
    }
}
