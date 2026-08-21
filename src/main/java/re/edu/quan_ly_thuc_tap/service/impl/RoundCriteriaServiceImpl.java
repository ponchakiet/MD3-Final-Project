package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.RoundCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.AssessmentRound;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;
import re.edu.quan_ly_thuc_tap.entity.RoundCriteria;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.RoundCriteriaMapper;
import re.edu.quan_ly_thuc_tap.repository.IAssessmentResultRepository;
import re.edu.quan_ly_thuc_tap.repository.IAssessmentRoundRepository;
import re.edu.quan_ly_thuc_tap.repository.IEvaluationCriteriaRepository;
import re.edu.quan_ly_thuc_tap.repository.IRoundCriteriaRepository;
import re.edu.quan_ly_thuc_tap.service.IRoundCriteriaService;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements IRoundCriteriaService {
    private final IRoundCriteriaRepository roundCriteriaRepository;
    private final IAssessmentRoundRepository assessmentRoundRepository;
    private final IEvaluationCriteriaRepository evaluationCriteriaRepository;
    private final IAssessmentResultRepository assessmentResultRepository;
    private final RoundCriteriaMapper roundCriteriaMapper;

    @Override
    public PageResponse<RoundCriteriaResponse> getAllCriteriaInRound(Long roundId, Pageable pageable) {
        Page<RoundCriteria> page = roundCriteriaRepository.findAllByRoundId(roundId, pageable);
        return PageResponseHelper.toPageResponse(page.map(roundCriteriaMapper::toResponse));
    }

    @Override
    public RoundCriteriaResponse getRoundCriterionById(Long roundCriterionId) {
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(roundCriterionId).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí với ID: " + roundCriterionId)
        );
        return roundCriteriaMapper.toResponse(roundCriteria);
    }

    @Override
    @Transactional
    public RoundCriteriaResponse addCriterionToRound(RoundCriteriaCreateRequestDTO dto) {
        // 1. Check trùng lặp
        if (roundCriteriaRepository.existsByRound_RoundIdAndCriterion_CriterionId(dto.getRoundId(), dto.getCriterionId())) {
            throw new DuplicateResourceException("Lỗi: Tiêu chí này đã được thêm vào đợt đánh giá rồi!");
        }

        AssessmentRound round = assessmentRoundRepository.findById(dto.getRoundId()).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy đợt đánh giá với ID: " + dto.getRoundId())
        );

        EvaluationCriteria criterion = evaluationCriteriaRepository.findById(dto.getCriterionId()).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí đánh giá với ID: " + dto.getCriterionId())
        );

        // 2. Validate Tổng trọng số không vượt quá 1.0
        BigDecimal currentTotalWeight = roundCriteriaRepository.sumWeightByRoundId(dto.getRoundId());
        if (currentTotalWeight == null) currentTotalWeight = BigDecimal.ZERO;

        BigDecimal newTotalWeight = currentTotalWeight.add(dto.getWeight());
        if (newTotalWeight.compareTo(BigDecimal.ONE) > 0) {
            throw new BadRequestException("Lỗi: Tổng trọng số vượt quá 1.0. Trọng số hiện tại của đợt này đã là: " + currentTotalWeight);
        }

        // 3. Lưu
        RoundCriteria roundCriteria = roundCriteriaMapper.toEntity(dto);
        roundCriteria.setRound(round);
        roundCriteria.setCriterion(criterion);

        RoundCriteria savedEntity = roundCriteriaRepository.save(roundCriteria);
        return roundCriteriaMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public RoundCriteriaResponse updateCriterionWeightInRound(Long roundCriterionId, RoundCriteriaUpdateRequestDTO dto) {
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(roundCriterionId).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy bản ghi cấu hình tiêu chí với ID: " + roundCriterionId)
        );

        // Validate Tổng trọng số không vượt quá 1.0
        BigDecimal currentTotalWeight = roundCriteriaRepository.sumWeightByRoundId(roundCriteria.getRound().getRoundId());
        if (currentTotalWeight == null) currentTotalWeight = BigDecimal.ZERO;

        // Trừ đi trọng số cũ, cộng thêm trọng số mới
        BigDecimal newTotalWeight = currentTotalWeight.subtract(roundCriteria.getWeight()).add(dto.getWeight());
        if (newTotalWeight.compareTo(BigDecimal.ONE) > 0) {
            throw new BadRequestException("Lỗi: Tổng trọng số sau khi cập nhật vượt quá 1.0! Vui lòng điều chỉnh lại.");
        }

        roundCriteriaMapper.updateEntityFromDto(dto, roundCriteria);
        roundCriteria.setUpdatedAt(LocalDateTime.now());
        RoundCriteria updatedEntity = roundCriteriaRepository.save(roundCriteria);

        return roundCriteriaMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void removeCriterionFromRound(Long roundCriterionId) {
        RoundCriteria roundCriteria = roundCriteriaRepository.findById(roundCriterionId).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy bản ghi cấu hình tiêu chí với ID: " + roundCriterionId)
        );

        // Check xem sinh viên đã bị chấm điểm theo tiêu chí này trong đợt này chưa
        boolean hasResults = assessmentResultRepository.existsByRound_RoundIdAndCriterion_CriterionId(
                roundCriteria.getRound().getRoundId(),
                roundCriteria.getCriterion().getCriterionId()
        );

        if (hasResults) {
            throw new BadRequestException("Lỗi: Không thể xóa tiêu chí này khỏi đợt vì đã có dữ liệu chấm điểm của sinh viên!");
        }

        roundCriteriaRepository.delete(roundCriteria);
    }
}
