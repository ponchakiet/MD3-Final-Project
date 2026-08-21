package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriterionDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentRoundResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.AssessmentRound;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;
import re.edu.quan_ly_thuc_tap.entity.InternshipPhase;
import re.edu.quan_ly_thuc_tap.entity.RoundCriteria;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.AssessmentRoundMapper;
import re.edu.quan_ly_thuc_tap.repository.*;
import re.edu.quan_ly_thuc_tap.service.IAssessmentRoundService;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements IAssessmentRoundService {
    private final IAssessmentRoundRepository assessmentRoundRepository;
    private final IInternshipPhaseRepository internshipPhaseRepository;
    private final IEvaluationCriteriaRepository evaluationCriteriaRepository;
    private final IRoundCriteriaRepository roundCriteriaRepository;
    private final IAssessmentResultRepository assessmentResultRepository;
    private final AssessmentRoundMapper assessmentRoundMapper;

    @Override
    public PageResponse<AssessmentRoundResponse> getAllRounds(Long phaseId, String keyword, Pageable pageable) {
        String searchKeyword = StringUtils.hasText(keyword) ? "%" + keyword.trim() + "%" : "%%";

        Page<AssessmentRound> page = assessmentRoundRepository.findAllRounds(phaseId, searchKeyword, pageable);

        return PageResponseHelper.toPageResponse(
                page.map(assessmentRoundMapper::toResponse)
        );
    }

    @Override
    public AssessmentRoundResponse getRoundById(Long id) {
        AssessmentRound round = assessmentRoundRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy đợt đánh giá với id: " + id)
        );
        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    @Transactional
    public AssessmentRoundResponse createRound(AssessmentRoundCreateRequestDTO dto) {
        // 1. Kiểm tra Phase có tồn tại không
        InternshipPhase phase = internshipPhaseRepository.findById(dto.getPhaseId()).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy giai đoạn thực tập với id: " + dto.getPhaseId())
        );

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Lỗi: Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
        }

        // 2. Tạo và lưu đợt đánh giá (AssessmentRound)
        AssessmentRound round = assessmentRoundMapper.toEntity(dto);
        round.setPhase(phase);
        AssessmentRound savedRound = assessmentRoundRepository.save(round);

        // 3. Lưu vào RoundCriteria
        List<RoundCriteria> roundCriteriaList = new ArrayList<>();

        for (RoundCriterionDTO rcDto : dto.getCriteriaList()) {
            EvaluationCriteria criteria = evaluationCriteriaRepository.findById(rcDto.getCriterionId()).orElseThrow(
                    () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí đánh giá với id: " + rcDto.getCriterionId())
            );

            RoundCriteria rc = RoundCriteria.builder()
                    .round(savedRound)
                    .criterion(criteria)
                    .weight(rcDto.getWeight())
                    .build();
            roundCriteriaList.add(rc);
        }

        // 4. Lưu
        roundCriteriaRepository.saveAll(roundCriteriaList);

        return assessmentRoundMapper.toResponse(savedRound);
    }

    @Override
    public AssessmentRoundResponse updateRound(Long id, AssessmentRoundUpdateRequestDTO dto) {
        AssessmentRound round = assessmentRoundRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy đợt đánh giá với id: " + id)
        );

        if (dto.getStartDate().isAfter(dto.getEndDate())) {
            throw new BadRequestException("Lỗi: Ngày bắt đầu không thể lớn hơn ngày kết thúc!");
        }

        if (!round.getPhase().getPhaseId().equals(dto.getPhaseId())) {
            InternshipPhase newPhase = internshipPhaseRepository.findById(dto.getPhaseId()).orElseThrow(
                    () -> new ResourceNotFoundException("Lỗi: Không tìm thấy giai đoạn thực tập với id: " + dto.getPhaseId())
            );
            round.setPhase(newPhase);
        }

        assessmentRoundMapper.updateEntityFromDto(dto, round);
        round.setUpdatedAt(LocalDateTime.now());
        AssessmentRound updatedRound = assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(updatedRound);
    }

    @Override
    public void deleteRound(Long id) {
        AssessmentRound round = assessmentRoundRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy đợt đánh giá với id: " + id)
        );

        // 1. Kiểm tra xem đợt này đã có điểm đánh giá nào được nhập chưa
        if (assessmentResultRepository.existsByRound_RoundId(id)) {
            throw new BadRequestException("Lỗi: Không thể xóa đợt đánh giá này vì đã có dữ liệu điểm của sinh viên!");
        }

        // 2. Xóa các tiêu chí trước thuộc round này trước

        roundCriteriaRepository.deleteAllByRound_RoundId(id);

        // 3. Xóa đợt đánh giá
        assessmentRoundRepository.delete(round);
    }
}
