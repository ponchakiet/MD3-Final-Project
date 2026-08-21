package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationCriteriaResponse {

    private Long criterionId;

    private String criterionName;

    private String description;

    private BigDecimal maxScore;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}