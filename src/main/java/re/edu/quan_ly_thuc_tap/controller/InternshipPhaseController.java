package re.edu.quan_ly_thuc_tap.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.ApiResponse;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipPhaseResponse;
import re.edu.quan_ly_thuc_tap.service.IInternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/internship-phases")
public class InternshipPhaseController {

    private final IInternshipPhaseService phaseService;

    //  Lấy danh sách
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getAllPhases() {
        List<InternshipPhaseResponse> data = phaseService.getAllPhases();
        return ResponseEntity.ok(
                ApiResponse.<List<InternshipPhaseResponse>>builder()
                        .success(true)
                        .message("Lấy danh sách giai đoạn thực tập thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // Lấy chi tiết 1 giai đọan
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MENTOR', 'STUDENT')")
    public ResponseEntity<?> getPhaseById(@PathVariable Long id) {
        InternshipPhaseResponse data = phaseService.getPhaseById(id);
        return ResponseEntity.ok(
                ApiResponse.<InternshipPhaseResponse>builder()
                        .success(true)
                        .message("Lấy thông tin chi tiết giai đoạn thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // Tạo mới (Chỉ ADMIN)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> createPhase(@Valid @RequestBody InternshipPhaseCreateRequestDTO dto) {
        InternshipPhaseResponse data = phaseService.createPhase(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InternshipPhaseResponse>builder()
                        .success(true)
                        .message("Tạo giai đoạn thực tập mới thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // Cập nhật (Chỉ ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> updatePhase(
            @PathVariable Long id,
            @Valid @RequestBody InternshipPhaseUpdateRequestDTO dto) {
        InternshipPhaseResponse data = phaseService.updatePhase(id, dto);
        return ResponseEntity.ok(
                ApiResponse.<InternshipPhaseResponse>builder()
                        .success(true)
                        .message("Cập nhật giai đoạn thực tập thành công!")
                        .data(data)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    // Xóa (Chỉ ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deletePhase(@PathVariable Long id) {
        phaseService.deletePhase(id);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Xóa giai đoạn thực tập thành công!")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}