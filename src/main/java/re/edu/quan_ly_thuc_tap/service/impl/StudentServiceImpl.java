package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.StudentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.StudentUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.StudentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.Student;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.StudentMapper;
import re.edu.quan_ly_thuc_tap.repository.IInternshipAssignmentRepository;
import re.edu.quan_ly_thuc_tap.repository.IStudentRepository;
import re.edu.quan_ly_thuc_tap.repository.IUserRepository;
import re.edu.quan_ly_thuc_tap.service.IStudentService;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements IStudentService {
    private final PasswordEncoder passwordEncoder;
    private final StudentMapper studentMapper;
    private final IUserRepository userRepository;
    private final IStudentRepository studentRepository;
    private final IInternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    public PageResponse<StudentResponse> findAllStudents(String keyword, Long userId,
                                                         RoleEnum role, Pageable pageable) {

        // 1. Kiểm tra xem có phải mentor không
        Long mentorId = role == RoleEnum.MENTOR ? userId : null;

        // 2. Lấy danh sách
        String searchKeyword = StringUtils.hasText(keyword) ? "%" + keyword.trim() + "%" : "%%";
        Page<Student> page = studentRepository.findAll(searchKeyword, mentorId, pageable);

        return PageResponseHelper.toPageResponse(
                page.map(studentMapper::toStudentResponse)
        );
    }

    @Override
    public StudentResponse findStudentById(Long studentId, Long userId, RoleEnum role) {

        // 1. Kiểm tra mã sinh viên
        Student s = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy sinh viên với id: " + studentId)
        );

        // 2. Phân quyền
        // STUDENT -> Chỉ được xem chính mình
        if (role == RoleEnum.STUDENT) {
            if (!studentId.equals(userId)) {
                throw new AccessDeniedException("Lỗi: Bạn không có quyền xem thông tin của sinh viên khác!");
            }
        }

        // MENTOR -> Chỉ được xem sinh viên mình đang hướng dẫn
        else if (role == RoleEnum.MENTOR) {
            boolean isAssigned = internshipAssignmentRepository
                    .existsByStudent_StudentIdAndMentor_MentorId(studentId, userId);
            if (!isAssigned) {
                throw new AccessDeniedException("Lỗi: Bạn không được phân công hướng dẫn sinh viên này!");
            }
        }

        return studentMapper.toStudentResponse(s);
    }

    @Override
    @Transactional
    public StudentResponse createStudent(StudentCreateRequestDTO dto) {
        // 1. Kiểm tra trùng lặp
        if (userRepository.existsByUserName(dto.getUserName())) throw new DuplicateResourceException("Lỗi: Tên đăng nhập đã tồn tại");
        if (userRepository.existsByEmail(dto.getEmail())) throw new DuplicateResourceException("Lỗi: Email đã tồn tại");
        if (studentRepository.existsByStudentCode(dto.getStudentCode())) throw new DuplicateResourceException("Lỗi: Mã sinh viên đã tồn tại");

        // 2. Tạo User
        User user = studentMapper.toUser(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(RoleEnum.STUDENT);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        // 3. Tạo Student liên kết với User
        Student student = studentMapper.toStudent(dto);
        student.setUser(savedUser);
        Student savedStudent = studentRepository.save(student);

        return studentMapper.toStudentResponse(savedStudent);
    }

    @Override
    @Transactional
    public StudentResponse updateStudent(Long studentId, StudentUpdateRequestDTO dto,
                                         Long currentUserId, RoleEnum role) {

        Student student = studentRepository.findById(studentId).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy sinh viên id: " + studentId));

        // STUDENT thì chỉ được sửa chính mình
        if (role == RoleEnum.STUDENT && !studentId.equals(currentUserId)) {
            throw new AccessDeniedException("Bạn không có quyền cập nhật thông tin người khác!");
        }


        studentMapper.updateUserFromDto(dto, student.getUser());
        studentMapper.updateStudentFromDto(dto, student);
        student.setUpdatedAt(LocalDateTime.now());

        return studentMapper.toStudentResponse(studentRepository.save(student));
    }
}
