package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentRoundResponse {

    private Long roundId;

    private Long phaseId;
    private String phaseName;

    private String roundName;

    private LocalDate startDate;

    private LocalDate endDate;

    private String description;

    private Boolean isActive;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}