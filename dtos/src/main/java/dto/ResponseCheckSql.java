package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseCheckSql {

    private Long taskUserId;

    private Status status;

    private Long executeTime;

    private String message;
}
