package re.edu.quan_ly_thuc_tap.dto.request;

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
public class RoundCriterionDTO {

    @NotNull(message = "ID tiêu chí không được để trống")
    private Long criterionId;

    @NotNull(message = "Trọng số không được để trống")
    @DecimalMin(value = "0.0", inclusive = false, message = "Trọng số phải lớn hơn 0")
    @Digits(integer = 3, fraction = 2, message = "Trọng số sai định dạng")
    private BigDecimal weight;
}