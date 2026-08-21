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
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentResultResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IAssessmentResultService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessment-results")
public class AssessmentResultController {

    private final IAssessmentResultService resultService;

    /**
     * Lấy danh sách kết quả đánh giá (lọc theo quyền, user_id, assignment_id)
     * ADMIN   : Xem tất cả
     * MENTOR  : Chỉ xem kết quả của assignment mình hướng dẫn
     * STUDENT : Chỉ xem kết quả của chính mình
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllResults(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<AssessmentResultResponse> data = resultService.getAllResults(
                userDetails.getUser().getUserId(),
                userDetails.getUser().getRole(),
                assignmentId,
                pageable
        );

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<AssessmentResultResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách kết quả đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Tạo kết quả đánh giá cho sinh viên
     * Chỉ MENTOR được phân công mới có quyền
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MENTOR')")
    public ResponseEntity<?> createResult(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @Valid @RequestBody AssessmentResultCreateRequestDTO dto
    ) {
        AssessmentResultResponse data = resultService.createResult(dto, userDetails.getUser());

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AssessmentResultResponse>builder()
                        .success(true)
                        .message("Tạo kết quả đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Cập nhật kết quả đánh giá
     * MENTOR chỉ được cập nhật kết quả do chính mình tạo
     */
    @PutMapping("/{result_id}")
    @PreAuthorize("hasAuthority('MENTOR')")
    public ResponseEntity<?> updateResult(
            @AuthenticationPrincipal UserDetailsCustom userDetails,
            @PathVariable("result_id") Long resultId,
            @Valid @RequestBody AssessmentResultUpdateRequestDTO dto
    ) {
        AssessmentResultResponse data = resultService.updateResult(resultId, dto, userDetails.getUser());

        return ResponseEntity.ok(
                ApiResponse.<AssessmentResultResponse>builder()
                        .success(true)
                        .message("Cập nhật kết quả đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}