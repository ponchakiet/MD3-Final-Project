package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternshipAssignmentCreateRequestDTO {

    @NotNull(message = "ID sinh viên không được để trống")
    private Long studentId;

    @NotNull(message = "ID giáo viên không được để trống")
    private Long mentorId;

    @NotNull(message = "ID giai đoạn thực tập không được để trống")
    private Long phaseId;
}