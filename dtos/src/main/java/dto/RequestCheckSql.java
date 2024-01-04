package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestCheckSql {

    private Long taskUserId;

    private String schema;

    private String userSql;

    private String mainSql;

    private String checkSql;
}
