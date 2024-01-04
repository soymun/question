package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestExecuteSql {

    private String schema;

    private String userSql;

    private Long userId;

    private Boolean admin;
}
