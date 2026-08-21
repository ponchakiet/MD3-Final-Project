package re.edu.quan_ly_thuc_tap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentUpdateStatusRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipAssignmentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IInternshipAssignmentService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internship_assignments")
public class InternshipAssignmentController {

    private final IInternshipAssignmentService assignmentService;

    /**
     * Lấy danh sách phân công thực tập (lọc theo quyền và user_id)
     * ADMIN   : Xem tất cả
     * MENTOR  : Chỉ xem phân công mình đang hướng dẫn
     * STUDENT : Chỉ xem phân công của chính mình
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllAssignments(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<InternshipAssignmentResponse> data = assignmentService.getAllAssignments(
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole(),
                pageable
        );
        return ResponseEntity.ok(
                ApiResponse.<PageResponse<InternshipAssignmentResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách phân công thực tập thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Lấy chi tiết một phân công thực tập theo ID (lọc theo quyền và user_id)
     * ADMIN   : Xem tất cả
     * MENTOR  : Chỉ xem nếu mình là người hướng dẫn trong phân công đó
     * STUDENT : Chỉ xem phân công của chính mình
     */
    @GetMapping("/{assignment_id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAssignmentById(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @PathVariable("assignment_id") Long assignmentId
    ) {
        InternshipAssignmentResponse data = assignmentService.getAssignmentById(
                assignmentId,
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole()
        );
        return ResponseEntity.ok(
                ApiResponse.<InternshipAssignmentResponse>builder()
                        .success(true)
                        .message("Lấy chi tiết phân công thực tập thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Tạo phân công thực tập mới (gán sinh viên cho giáo viên trong giai đoạn)
     * Chỉ ADMIN
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createAssignment(
            @Valid @RequestBody InternshipAssignmentCreateRequestDTO dto
    ) {
        InternshipAssignmentResponse data = assignmentService.createAssignment(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InternshipAssignmentResponse>builder()
                        .success(true)
                        .message("Tạo phân công thực tập mới thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Cập nhật trạng thái phân công thực tập (PENDING, IN_PROGRESS, ...)
     * Chỉ ADMIN
     */
    @PutMapping("/{assignment_id}/status")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateStatus(
            @PathVariable("assignment_id") Long assignmentId,
            @Valid @RequestBody InternshipAssignmentUpdateStatusRequestDTO dto
    ) {
        InternshipAssignmentResponse data = assignmentService.updateStatus(assignmentId, dto);

        return ResponseEntity.ok(
                ApiResponse.<InternshipAssignmentResponse>builder()
                        .success(true)
                        .message("Cập nhật trạng thái phân công thực tập thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
