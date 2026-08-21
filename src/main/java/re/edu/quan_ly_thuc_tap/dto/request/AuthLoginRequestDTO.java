package re.edu.quan_ly_thuc_tap.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthLoginRequestDTO {

    @NotBlank(message = "Username không được để trống")
    private String userName;

    @NotBlank(message = "Password không được để trống")
    private String password;
}