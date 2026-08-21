package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.quan_ly_thuc_tap.dto.request.UserCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateRoleRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateStatusRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.UserMapper;
import re.edu.quan_ly_thuc_tap.repository.IInternshipAssignmentRepository;
import re.edu.quan_ly_thuc_tap.repository.IMentorRepository;
import re.edu.quan_ly_thuc_tap.repository.IStudentRepository;
import re.edu.quan_ly_thuc_tap.repository.IUserRepository;
import re.edu.quan_ly_thuc_tap.service.IUserService;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final IUserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final IMentorRepository mentorRepository;
    private final IStudentRepository studentRepository;
    private final IInternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    public PageResponse<UserResponse> getAllUsers(RoleEnum role, Pageable pageable) {
        Page<User> users = (role != null)
                ? userRepository.findByRole(role, pageable)
                : userRepository.findAll(pageable);
        return PageResponseHelper.toPageResponse(users.map(userMapper::toUserResponse));
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy user với id: " + userId));
        return userMapper.toUserResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequestDTO request) {

        // 1. Kiểm tra trùng username
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new DuplicateResourceException("Lỗi: Username '" + request.getUserName() + "' đã tồn tại");
        }

        // 2. Kiểm tra trùng email
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Lỗi: Email '" + request.getEmail() + "' đã được sử dụng");
        }

        // 3. Kiểm tra trùng phoneNumber
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new DuplicateResourceException("Lỗi: Phone number '" + request.getPhoneNumber() + "' đã được sử dụng");
        }

        // 4. Tạo user mới
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long userId, UserUpdateRequestDTO request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy user với id: " + userId));

        // 1. Kiểm tra email mới
        if (!user.getEmail().equals(request.getEmail())
                && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Lỗi: Email '" + request.getEmail() + "' đã được sử dụng");
        }

        // 2. Cập nhật
        userMapper.updateEntityFromDTO(request, user);
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateStatus(Long userId, UserUpdateStatusRequestDTO request, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy user với id: " + userId));

        // Không cho khóa chính mình
        if (user.getUserId().equals(currentUserId)) {
            throw new BadRequestException("Lỗi: Không thể thay đổi trạng thái tài khoản của chính mình");
        }

        user.setIsActive(request.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateRole(Long userId, UserUpdateRoleRequestDTO request) {
        User target = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy user với id: " + userId));

        // ADMIN không được thay đổi role của ADMIN khác
        if (target.getRole() == RoleEnum.ADMIN) {
            throw new BadRequestException("Lỗi: Không được phép thay đổi vai trò của tài khoản ADMIN");
        }

        target.setRole(request.getRole());
        target.setUpdatedAt(LocalDateTime.now());
        return userMapper.toUserResponse(userRepository.save(target));
    }


    @Override
    @Transactional
    public void deleteUser(Long userId, Long currentUserId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Lỗi: Không tìm thấy user với id: " + userId));

        if (user.getUserId().equals(currentUserId)) {
            throw new BadRequestException("Lỗi: Không thể xóa tài khoản của chính mình");
        }

        if (user.getRole() == RoleEnum.MENTOR) {
            if (internshipAssignmentRepository.existsByMentor_MentorId(userId)) {
                throw new BadRequestException(
                        "Lỗi: Không thể xóa mentor này vì đang được phân công dẫn dắt sinh viên thực tập!"
                );
            }
            mentorRepository.deleteById(userId);

        } else if (user.getRole() == RoleEnum.STUDENT) {
            if (internshipAssignmentRepository.existsByStudent_StudentId(userId)) {
                throw new BadRequestException(
                        "Lỗi: Không thể xóa sinh viên này vì đã có dữ liệu phân công thực tập!"
                );
            }
            studentRepository.deleteById(userId);
        }

        userRepository.delete(user);
    }
}
