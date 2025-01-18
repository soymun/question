package com.example.site.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CodeExecuteResponse extends ResponseCheck {

    private Long userId;

    private Long taskId;

    private Long time;

    private Long attempt;
}
