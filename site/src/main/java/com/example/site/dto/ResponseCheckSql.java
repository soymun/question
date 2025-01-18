package com.example.site.dto;

import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCheckSql extends ResponseCheck {

    private Long taskUserId;

    private Long executeTime;
}
