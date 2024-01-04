package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CodeExecuteRequest {

    private Long userId;

    private Long taskId;

    private String checkClass;

    private String userClass;

    private String checkCode;

    private String userCode;

    private Long attempt;
}
