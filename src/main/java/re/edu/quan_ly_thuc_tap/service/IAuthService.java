package re.edu.quan_ly_thuc_tap.service;

import re.edu.quan_ly_thuc_tap.dto.request.AuthLoginRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AuthRefreshRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AuthResponse;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.entity.User;

public interface IAuthService {
    AuthResponse login (AuthLoginRequestDTO request);
    UserResponse getMyInfo(User currentUser);
    AuthResponse refreshToken(AuthRefreshRequestDTO request);
    void logout(User currentUser);
}
