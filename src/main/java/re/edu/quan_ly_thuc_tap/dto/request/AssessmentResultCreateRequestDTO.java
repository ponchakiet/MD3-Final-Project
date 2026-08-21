package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentResultCreateRequestDTO {

    @NotNull(message = "ID phân công thực tập không được để trống")
    private Long assignmentId;

    @NotNull(message = "ID đợt đánh giá không được để trống")
    private Long roundId;

    @NotNull(message = "ID tiêu chí đánh giá không được để trống")
    private Long criterionId;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(value = "0.0", message = "Điểm không được âm")
    private BigDecimal score;

    private String comments;
}