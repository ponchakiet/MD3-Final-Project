package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;
import re.edu.quan_ly_thuc_tap.util.enums.AssignmentStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InternshipAssignmentResponse {

    private Long assignmentId;
    private StudentSummary student;
    private MentorSummary mentor;
    private PhaseSummary phase;
    private LocalDateTime assignedDate;
    private AssignmentStatusEnum status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ───  Student ──────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StudentSummary {
        private Long studentId;
        private String studentCode;
        private String fullName;   // từ User.fullName
        private String email;      // từ User.email
        private String major;
        private String className;
    }

    // ─── Mentor ───────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MentorSummary {
        private Long mentorId;
        private String fullName;    // từ User.fullName
        private String email;       // từ User.email
        private String department;
        private String academicRank;
    }

    // ─── Phase ────────────────────────────────────────────────
    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PhaseSummary {
        private Long phaseId;
        private String phaseName;
        private LocalDate startDate;
        private LocalDate endDate;
    }
}
