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
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentRoundResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IAssessmentRoundService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/assessment-rounds")
public class AssessmentRoundController {

    private final IAssessmentRoundService assessmentRoundService;

    /**
     * Lấy danh sách tất cả các đợt đánh giá (có thể lọc theo phase_id)
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllRounds(
            @RequestParam(required = false) Long phaseId,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<AssessmentRoundResponse> data = assessmentRoundService.getAllRounds(phaseId, keyword, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<AssessmentRoundResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách đợt đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Lấy thông tin chi tiết một đợt đánh giá theo ID
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getRoundById(@PathVariable Long id) {
        AssessmentRoundResponse data = assessmentRoundService.getRoundById(id);

        return ResponseEntity.ok(
                ApiResponse.<AssessmentRoundResponse>builder()
                        .success(true)
                        .message("Lấy thông tin chi tiết đợt đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Tạo một đợt đánh giá mới (kèm danh sách tiêu chí và trọng số)
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createRound(
            @Valid @RequestBody AssessmentRoundCreateRequestDTO dto) {
        AssessmentRoundResponse data = assessmentRoundService.createRound(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<AssessmentRoundResponse>builder()
                        .success(true)
                        .message("Tạo đợt đánh giá mới thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Cập nhật thông tin cơ bản một đợt đánh giá
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateRound(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRoundUpdateRequestDTO dto
    ) {
        AssessmentRoundResponse data = assessmentRoundService.updateRound(id, dto);

        return ResponseEntity.ok(
                ApiResponse.<AssessmentRoundResponse>builder()
                        .success(true)
                        .message("Cập nhật thông tin đợt đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Xóa một đợt đánh giá
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteRound(@PathVariable Long id) {
        assessmentRoundService.deleteRound(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Xóa đợt đánh giá thành công!")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}