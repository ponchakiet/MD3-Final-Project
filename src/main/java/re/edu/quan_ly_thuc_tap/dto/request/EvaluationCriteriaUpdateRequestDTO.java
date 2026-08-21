package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EvaluationCriteriaUpdateRequestDTO {

    @NotBlank(message = "Tên tiêu chí đánh giá không được để trống")
    @Size(max = 200, message = "Tên tiêu chí đánh giá không được vượt quá 200 ký tự")
    private String criterionName;

    private String description;

    @NotNull(message = "Điểm tối đa không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Điểm tối đa phải lớn hơn 0")
    @DecimalMax(value = "100.0", inclusive = true, message = "Điểm tối đa không được vượt quá 100") // Dòng mới thêm vào
    @Digits(integer = 3, fraction = 2, message = "Điểm tối đa chỉ được phép có tối đa 3 chữ số phần nguyên và 2 chữ số phần thập phân")
    private BigDecimal maxScore;
}