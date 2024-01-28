package dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestCheckSql {

    private Long taskUserId;

    private String schema;

    private String userSql;

    private String mainSql;

    private String checkSql;
}
