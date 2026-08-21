package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentResultResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.*;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.AssessmentResultMapper;
import re.edu.quan_ly_thuc_tap.repository.*;
import re.edu.quan_ly_thuc_tap.service.IAssessmentResultService;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl implements IAssessmentResultService {

    private final AssessmentResultMapper resultMapper;
    private final IAssessmentResultRepository resultRepository;
    private final IInternshipAssignmentRepository assignmentRepository;
    private final IAssessmentRoundRepository roundRepository;
    private final IEvaluationCriteriaRepository criteriaRepository;
    private final IRoundCriteriaRepository roundCriteriaRepository;

    // ─── GET ALL ─────────────────────────────────────────────────────────────
    @Override
    public PageResponse<AssessmentResultResponse> getAllResults(
            Long userId, RoleEnum role, Long assignmentId, Pageable pageable) {

        Long mentorId  = null;
        Long studentId = null;

        if (role == RoleEnum.MENTOR) {
            mentorId = userId;
        } else if (role == RoleEnum.STUDENT) {
            studentId = userId;
        }
        // ADMIN: cả 2 null → lấy tất cả

        Page<AssessmentResult> page = resultRepository.findAllByFilter(
                mentorId, studentId, assignmentId, pageable
        );

        return PageResponseHelper.toPageResponse(page.map(resultMapper::toResponse));
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public AssessmentResultResponse createResult(
            AssessmentResultCreateRequestDTO dto, User currentUser) {

        // 1. Kiểm tra assignment tồn tại
        InternshipAssignment assignment = assignmentRepository.findByIdWithDetails(dto.getAssignmentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy phân công thực tập với id: " + dto.getAssignmentId()));

        // 2. MENTOR chỉ được chấm điểm cho assignment mà mình là người hướng dẫn
        if (!assignment.getMentor().getMentorId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException(
                    "Lỗi: Bạn không được phân công hướng dẫn sinh viên này!");
        }

        // 3. Kiểm tra round tồn tại
        AssessmentRound round = roundRepository.findById(dto.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy đợt đánh giá với id: " + dto.getRoundId()));

        // 4. Round phải thuộc cùng Phase với assignment
        if (!round.getPhase().getPhaseId().equals(assignment.getPhase().getPhaseId())) {
            throw new BadRequestException(
                    "Lỗi: Đợt đánh giá này không thuộc giai đoạn thực tập của sinh viên!");
        }

        // 5. Kiểm tra criterion tồn tại
        EvaluationCriteria criterion = criteriaRepository.findById(dto.getCriterionId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy tiêu chí đánh giá với id: " + dto.getCriterionId()));

        // 6. Tiêu chí phải được cấu hình trong đợt đánh giá đó (có trong RoundCriteria)
        if (!roundCriteriaRepository.existsByRound_RoundIdAndCriterion_CriterionId(
                dto.getRoundId(), dto.getCriterionId())) {
            throw new BadRequestException(
                    "Lỗi: Tiêu chí này chưa được cấu hình trong đợt đánh giá!");
        }

        // 7. Kiểm tra điểm không vượt quá maxScore
        if (dto.getScore().compareTo(criterion.getMaxScore()) > 0) {
            throw new BadRequestException(
                    "Lỗi: Điểm không được vượt quá điểm tối đa (" + criterion.getMaxScore() + ")!");
        }

        // 8. Kiểm tra unique (assignment, round, criterion)
        if (resultRepository.existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
                dto.getAssignmentId(), dto.getRoundId(), dto.getCriterionId())) {
            throw new DuplicateResourceException(
                    "Lỗi: Tiêu chí này đã được chấm điểm cho sinh viên trong đợt đánh giá này rồi!");
        }

        // 9. Tạo và lưu
        AssessmentResult result = AssessmentResult.builder()
                .assignment(assignment)
                .round(round)
                .criterion(criterion)
                .score(dto.getScore())
                .comments(dto.getComments())
                .evaluatedBy(currentUser)
                .build();

        AssessmentResult saved = resultRepository.save(result);

        // Fetch lại để tránh n+1
        return resultMapper.toResponse(
                resultRepository.findByIdWithDetails(saved.getResultId()).orElseThrow()
        );
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public AssessmentResultResponse updateResult(
            Long resultId, AssessmentResultUpdateRequestDTO dto, User currentUser) {

        AssessmentResult result = resultRepository.findByIdWithDetails(resultId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy kết quả đánh giá với id: " + resultId));

        // MENTOR chỉ được cập nhật kết quả do chính mình tạo
        if (!result.getEvaluatedBy().getUserId().equals(currentUser.getUserId())) {
            throw new AccessDeniedException(
                    "Lỗi: Bạn không có quyền cập nhật kết quả đánh giá này!");
        }

        // Kiểm tra điểm không vượt quá maxScore
        if (dto.getScore().compareTo(result.getCriterion().getMaxScore()) > 0) {
            throw new BadRequestException(
                    "Lỗi: Điểm không được vượt quá điểm tối đa ("
                            + result.getCriterion().getMaxScore() + ")!");
        }

        resultMapper.updateEntityFromDto(dto, result);
        result.setUpdatedAt(LocalDateTime.now());
        resultRepository.save(result);

        return resultMapper.toResponse(result);
    }
}
