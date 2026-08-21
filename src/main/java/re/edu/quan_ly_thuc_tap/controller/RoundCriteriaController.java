package re.edu.quan_ly_thuc_tap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.RoundCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.service.IRoundCriteriaService;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/round-criteria")
public class RoundCriteriaController {
    private final IRoundCriteriaService roundCriteriaService;

    /**
     * Lấy danh sách các tiêu chí trong một đợt đánh giá (truyền roundId)
     */
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllCriteriaInRound(
            @RequestParam(name = "roundId", required = true) Long roundId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Sort sort = Sort.by(direction, sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        PageResponse<RoundCriteriaResponse> data = roundCriteriaService.getAllCriteriaInRound(roundId, pageable);

        return ResponseEntity.ok(
                ApiResponse.<PageResponse<RoundCriteriaResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách cấu hình tiêu chí thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    /**
     * Lấy chi tiết một bản ghi bằng ID (RoundCriteriaId)
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getRoundCriterionById(
            @PathVariable("id") Long roundCriterionId
    ) {
        RoundCriteriaResponse data = roundCriteriaService.getRoundCriterionById(roundCriterionId);

        return ResponseEntity.ok(
                ApiResponse.<RoundCriteriaResponse>builder()
                        .success(true)
                        .message("Lấy thông tin chi tiết cấu hình tiêu chí thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> addCriterionToRound(
            @Valid @RequestBody RoundCriteriaCreateRequestDTO dto) {
        RoundCriteriaResponse data = roundCriteriaService.addCriterionToRound(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<RoundCriteriaResponse>builder()
                        .success(true)
                        .message("Thêm tiêu chí vào đợt đánh giá thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updateCriterionWeightInRound(
            @PathVariable("id") Long roundCriterionId,
            @Valid @RequestBody RoundCriteriaUpdateRequestDTO dto
    ) {
        RoundCriteriaResponse data = roundCriteriaService.updateCriterionWeightInRound(roundCriterionId, dto);

        return ResponseEntity.ok(
                ApiResponse.<RoundCriteriaResponse>builder()
                        .success(true)
                        .message("Cập nhật trọng số tiêu chí thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> removeCriterionFromRound(@PathVariable("id") Long roundCriterionId) {
        roundCriteriaService.removeCriterionFromRound(roundCriterionId);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Xóa tiêu chí khỏi đợt đánh giá thành công!")
                        .data(null)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
