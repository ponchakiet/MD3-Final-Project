package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoundCriteriaCreateRequestDTO {

    @NotNull(message = "ID đợt đánh giá không được để trống")
    private Long roundId;

    @NotNull(message = "ID tiêu chí đánh giá không được để trống")
    private Long criterionId;

    @NotNull(message = "Trọng số không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Trọng số phải lớn hơn 0")
    @DecimalMax(value = "1.0", inclusive = true, message = "Trọng số không được vượt quá 1.0 (Ví dụ: 0.2, 0.3)")
    @Digits(integer = 1, fraction = 2, message = "Trọng số sai định dạng")
    private BigDecimal weight;
}