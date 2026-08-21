package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoundCriteriaResponse {

    private Long roundCriterionId;

    // Thông tin của AssessmentRound
    private Long roundId;
    private String roundName;

    // Thông tin của EvaluationCriteria
    private Long criterionId;
    private String criterionName;
    private BigDecimal maxScore;

    private BigDecimal weight;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}