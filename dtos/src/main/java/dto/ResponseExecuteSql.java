package dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class ResponseExecuteSql {

    private Long userId;

    private List<Map<String, Object>> resultSelect;

    private Integer resultOther;

    private Long time;

    private String message;
}
