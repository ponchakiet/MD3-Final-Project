package re.edu.quan_ly_thuc_tap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "assessment_results",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_assignment_round_criterion",
                columnNames = {"assignment_id", "round_id", "criterion_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    // FK tới InternshipAssignment
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false)
    private InternshipAssignment assignment;

    // FK tới AssessmentRound
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private AssessmentRound round;

    // FK tới EvaluationCriteria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterion_id", nullable = false)
    private EvaluationCriteria criterion;

    // Điểm đạt được cho tiêu chí này
    @Column(name = "score", precision = 5, scale = 2, nullable = false)
    private BigDecimal score;

    // Nhận xét của mentor
    @Column(name = "comments", columnDefinition = "TEXT")
    private String comments;

    // Mentor nào đã đánh giá — FK tới User (phải có role MENTOR)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluated_by", nullable = false)
    private User evaluatedBy;

    @CreationTimestamp
    @Column(name = "evaluation_date", updatable = false)
    private LocalDateTime evaluationDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}