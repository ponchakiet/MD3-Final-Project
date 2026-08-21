package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.StudentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.StudentUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.StudentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.Student;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

public interface IStudentService {
    PageResponse<StudentResponse> findAllStudents(String keyword, Long userId, RoleEnum role, Pageable pageable);
    StudentResponse findStudentById(Long studentId, Long userId, RoleEnum role);
    StudentResponse createStudent(StudentCreateRequestDTO dto);
    StudentResponse updateStudent(Long studentId, StudentUpdateRequestDTO dto, Long currentUserId, RoleEnum role);
}
