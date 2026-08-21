package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipPhaseResponse;
import re.edu.quan_ly_thuc_tap.entity.InternshipPhase;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.InternshipPhaseMapper;
import re.edu.quan_ly_thuc_tap.repository.IAssessmentRoundRepository;
import re.edu.quan_ly_thuc_tap.repository.IInternshipAssignmentRepository;
import re.edu.quan_ly_thuc_tap.repository.IInternshipPhaseRepository;
import re.edu.quan_ly_thuc_tap.service.IInternshipPhaseService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements IInternshipPhaseService {
    private final InternshipPhaseMapper internshipPhaseMapper;
    private final IInternshipPhaseRepository internshipPhaseRepository;
    private final IInternshipAssignmentRepository assignmentRepository;
    private final IAssessmentRoundRepository assessmentRoundRepository;



    @Override
    public List<InternshipPhaseResponse> getAllPhases() {
        List<InternshipPhase> phases =internshipPhaseRepository.findAll();
        return phases.stream()
                .map(internshipPhaseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Long phaseId) {
        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId));

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    @Transactional
    public InternshipPhaseResponse createPhase(InternshipPhaseCreateRequestDTO dto) {
        // 1. Kiểm tra logic ngày tháng
        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Lỗi: Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
        }

        // 2. Kiểm tra trùng lặp tên giai đoạn
        if (internshipPhaseRepository.existsByPhaseName(dto.getPhaseName())) {
            throw new DuplicateResourceException("Lỗi: Tên giai đoạn thực tập đã tồn tại!");
        }

        // 3. Lưu DB
        InternshipPhase phase = internshipPhaseMapper.toEntity(dto);
        return internshipPhaseMapper.toResponse(internshipPhaseRepository.save(phase));
    }

    @Override
    @Transactional
    public InternshipPhaseResponse updatePhase(Long phaseId, InternshipPhaseUpdateRequestDTO dto) {
        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId));

        // 1. Kiểm tra logic ngày tháng
        if (dto.getStartDate() != null && dto.getEndDate() != null) {
            if (dto.getStartDate().isAfter(dto.getEndDate())) {
                throw new BadRequestException("Lỗi: Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
            }
        }

        // 2. Kiểm tra trùng lặp tên (chỉ check nếu có đổi tên mới)
        if (dto.getPhaseName() != null && !dto.getPhaseName().equals(phase.getPhaseName())) {
            if (internshipPhaseRepository.existsByPhaseName(dto.getPhaseName())) {
                throw new DuplicateResourceException("Lỗi: Tên giai đoạn thực tập đã tồn tại!");
            }
        }

        // 3. Cập nhật & Lưu DB
        internshipPhaseMapper.updateEntityFromDto(dto, phase);
        phase.setUpdatedAt(LocalDateTime.now());
        return internshipPhaseMapper.toResponse(internshipPhaseRepository.save(phase));
    }

    @Override
    @Transactional
    public void deletePhase(Long phaseId) {
        InternshipPhase phase = internshipPhaseRepository.findById(phaseId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy giai đoạn thực tập với id: " + phaseId));


        // Check 1: Đã có sinh viên phân công chưa
        if (assignmentRepository.existsByPhase_PhaseId(phaseId)) {
            throw new BadRequestException(
                    "Lỗi: Không thể xóa vì đã có sinh viên được phân công vào giai đoạn này!");
        }

        // Check 2: Đã có đợt đánh giá chưa
        if (assessmentRoundRepository.existsByPhase_PhaseId(phaseId)) {
            throw new BadRequestException(
                    "Lỗi: Không thể xóa vì đã có đợt đánh giá được tạo trong giai đoạn này!");
        }



       internshipPhaseRepository.delete(phase);
    }
}