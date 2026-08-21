package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateStatusRequestDTO {

    @NotNull(message = "Trạng thái isActive không được để trống")
    private Boolean isActive;
}