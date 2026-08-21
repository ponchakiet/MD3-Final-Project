package re.edu.quan_ly_thuc_tap.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "round_criteria",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_round_criterion",
                columnNames = {"round_id", "criterion_id"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoundCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "round_criterion_id")
    private Long roundCriterionId;

    // Quan hệ 1-N với AssessmentRound
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "round_id", nullable = false)
    private AssessmentRound round;

    // Quan hệ 1-N với EvaluationCriteria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criterion_id", nullable = false)
    private EvaluationCriteria criterion;

    // Trọng số riêng của tiêu chí này trong đợt đánh giá này
    // (cùng 1 tiêu chí có thể có trọng số khác nhau ở các đợt khác nhau)
    @Column(name = "weight", precision = 5, scale = 2, nullable = false)
    private BigDecimal weight;

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
