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
import org.springframework.web.bind.annotation.*;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.EvaluationCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IEvaluationCriteriaService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation-criteria")
public class EvaluationCriteriaController {

    private final IEvaluationCriteriaService evaluationCriteriaService;

    /**
     * Lấy danh sách tất cả các tiêu chí đánh giá
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllCriteria(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<EvaluationCriteriaResponse> data = evaluationCriteriaService.findAllCriteria(keyword, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<EvaluationCriteriaResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách tiêu chí đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Lấy thông tin chi tiết một tiêu chí đánh giá theo ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getCriteriaById(@PathVariable Long id) {
        EvaluationCriteriaResponse data = evaluationCriteriaService.findCriteriaById(id);

        return ResponseEntity.ok(
                ApiResponse.<EvaluationCriteriaResponse>builder()
                        .success(true)
                        .message("Lấy thông tin chi tiết tiêu chí đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Tạo một tiêu chí đánh giá mới
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createCriteria(
            @Valid @RequestBody EvaluationCriteriaCreateRequestDTO dto) {
        EvaluationCriteriaResponse data = evaluationCriteriaService.createCriteria(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<EvaluationCriteriaResponse>builder()
                        .success(true)
                        .message("Tạo tiêu chí đánh giá mới thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Cập nhật thông tin một tiêu chí đánh giá
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCriteria(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationCriteriaUpdateRequestDTO dto
    ) {
        EvaluationCriteriaResponse data = evaluationCriteriaService.updateCriteria(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<EvaluationCriteriaResponse>builder()
                        .success(true)
                        .message("Cập nhật tiêu chí đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Xóa một tiêu chí đánh giá
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteCriteria(@PathVariable Long id) {
        evaluationCriteriaService.deleteCriteria(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Xóa tiêu chí đánh giá thành công!")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}