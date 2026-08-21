package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.UserCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateRoleRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateStatusRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

public interface IUserService {
    PageResponse<UserResponse> getAllUsers(RoleEnum role, Pageable pageable);
    UserResponse getUserById(Long userId);
    UserResponse createUser(UserCreateRequestDTO request);
    UserResponse updateUser(Long userId, UserUpdateRequestDTO request);
    UserResponse updateStatus(Long userId, UserUpdateStatusRequestDTO request, Long currentUserId);
    UserResponse updateRole(Long userId, UserUpdateRoleRequestDTO request);
    void deleteUser(Long userId, Long currentUserId);
}
