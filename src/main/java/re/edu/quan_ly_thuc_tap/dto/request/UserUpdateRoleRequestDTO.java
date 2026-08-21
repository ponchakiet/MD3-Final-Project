package re.edu.quan_ly_thuc_tap.dto.request;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRoleRequestDTO {

    @NotNull(message = "Role không được để trống")
    private RoleEnum role;
}