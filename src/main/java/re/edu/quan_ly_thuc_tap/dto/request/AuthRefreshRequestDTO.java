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
public class AuthRefreshRequestDTO {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}