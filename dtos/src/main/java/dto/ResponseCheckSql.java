package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseCheckSql {

    private Long taskUserId;

    private Status status;

    private Long executeTime;

    private String message;
}
