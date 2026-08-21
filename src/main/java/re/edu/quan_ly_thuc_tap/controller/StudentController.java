package re.edu.quan_ly_thuc_tap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.StudentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.StudentUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.StudentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IStudentService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/students")
public class StudentController {
    private final IStudentService studentService;

    /**
     * MENTOR: Chỉ xem sinh viên được phân công
     * ADMIN: Xem tất cả
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR')")
    public ResponseEntity<?> getAllStudents(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);
        PageResponse<StudentResponse> data = studentService.findAllStudents(
                keyword,
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole(),
                pageable
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<StudentResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách sinh viên thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * STUDENT: Chỉ xem của mình
     * MENTOR: Chỉ xem sinh viên được phân công (đề ko có nhưng em thêm để tránh xung đột với API phía trên)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getStudentById(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @PathVariable Long id
    ) {
        StudentResponse data = studentService.findStudentById(
                id,
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole()
        );
        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Lấy thông tin chi tiết sinh viên thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Chỉ ADMIN mới có quyền tạo
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createStudent(
            @Valid @RequestBody StudentCreateRequestDTO dto) {
        StudentResponse data = studentService.createStudent(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Tạo sinh viên mới thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * STUDENT: Chỉ cập nhật của mình
     * ADMIN: Có quyền cập nhật bất kỳ ai
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'STUDENT')")
    public ResponseEntity<?> updateStudent(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequestDTO dto
    ) {
        StudentResponse data = studentService.updateStudent(
                id, dto,
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole()
        );
        return ResponseEntity.ok(
                ApiResponse.<StudentResponse>builder()
                        .success(true)
                        .message("Cập nhật thông tin sinh viên thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
