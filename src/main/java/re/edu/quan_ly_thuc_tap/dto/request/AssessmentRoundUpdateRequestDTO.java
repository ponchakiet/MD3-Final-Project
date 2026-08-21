package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssessmentRoundUpdateRequestDTO {

    @NotNull(message = "Phase ID không được để trống")
    private Long phaseId;

    @NotBlank(message = "Tên đợt đánh giá không được để trống")
    @Size(max = 100, message = "Tên đợt đánh giá không vượt quá 100 ký tự")
    private String roundName;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc không được để trống")
    private LocalDate endDate;

    private String description;

    private Boolean isActive;
}