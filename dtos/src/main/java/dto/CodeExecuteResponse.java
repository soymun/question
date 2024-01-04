package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeExecuteResponse {

    private Status status;

    private String message;

    private Long userId;

    private Long taskId;

    private Long time;

    private Long attempt;
}
