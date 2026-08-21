package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentResultResponse {

    private Long resultId;
    private AssignmentSummary assignment;
    private RoundSummary round;
    private CriterionSummary criterion;
    private BigDecimal score;
    private String comments;
    private EvaluatedBySummary evaluatedBy;
    private LocalDateTime evaluationDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ─── Assignment───────────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AssignmentSummary {
        private Long assignmentId;
        private String studentCode;     // từ assignment.student.studentCode
        private String studentName;     // từ assignment.student.user.fullName
        private String mentorName;      // từ assignment.mentor.user.fullName
        private String phaseName;       // từ assignment.phase.phaseName
    }

    // ─── Round ────────────────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RoundSummary {
        private Long roundId;
        private String roundName;
        private LocalDate startDate;
        private LocalDate endDate;
    }

    // ─── Criterion ────────────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CriterionSummary {
        private Long criterionId;
        private String criterionName;
        private BigDecimal maxScore;
    }

    // ─── EvaluatedBy (User) ─────────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EvaluatedBySummary {
        private Long userId;
        private String fullName;
        private String email;
    }
}