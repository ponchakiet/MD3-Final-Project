package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import re.edu.quan_ly_thuc_tap.util.enums.AssignmentStatusEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InternshipAssignmentUpdateStatusRequestDTO {

    @NotNull(message = "Trạng thái không được để trống")
    private AssignmentStatusEnum status;
}