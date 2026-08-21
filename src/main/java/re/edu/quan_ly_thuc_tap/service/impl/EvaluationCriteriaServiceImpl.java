package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.EvaluationCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.EvaluationCriteriaMapper;
import re.edu.quan_ly_thuc_tap.repository.IAssessmentResultRepository;
import re.edu.quan_ly_thuc_tap.repository.IEvaluationCriteriaRepository;
import re.edu.quan_ly_thuc_tap.repository.IRoundCriteriaRepository;
import re.edu.quan_ly_thuc_tap.service.IEvaluationCriteriaService;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements IEvaluationCriteriaService {
    private final IAssessmentResultRepository assessmentResultRepository;
    private final IRoundCriteriaRepository roundCriteriaRepository;
    private final IEvaluationCriteriaRepository evaluationCriteriaRepository;
    private final EvaluationCriteriaMapper evaluationCriteriaMapper;

    @Override
    public PageResponse<EvaluationCriteriaResponse> findAllCriteria(String keyword, Pageable pageable) {
        String searchKeyword = StringUtils.hasText(keyword) ? "%" + keyword.trim() + "%" : "%%";

        Page<EvaluationCriteria> page = evaluationCriteriaRepository.findAllCriteria(searchKeyword, pageable);

        return PageResponseHelper.toPageResponse(
                page.map(evaluationCriteriaMapper::toResponse)
        );
    }

    @Override
    public EvaluationCriteriaResponse findCriteriaById(Long id) {
        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí đánh giá với id: " + id)
        );
        return evaluationCriteriaMapper.toResponse(criteria);
    }

    @Override
    @Transactional
    public EvaluationCriteriaResponse createCriteria(EvaluationCriteriaCreateRequestDTO dto) {
        if (evaluationCriteriaRepository.existsByCriterionName(dto.getCriterionName())) {
            throw new DuplicateResourceException("Lỗi: Tên tiêu chí đánh giá đã tồn tại trong hệ thống!");
        }

        EvaluationCriteria entity = evaluationCriteriaMapper.toEntity(dto);
        EvaluationCriteria savedEntity = evaluationCriteriaRepository.save(entity);

        return evaluationCriteriaMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public EvaluationCriteriaResponse updateCriteria(Long id, EvaluationCriteriaUpdateRequestDTO dto) {
        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí đánh giá với id: " + id)
        );

        if (evaluationCriteriaRepository.existsByCriterionNameAndCriterionIdNot(dto.getCriterionName(), id)) {
            throw new DuplicateResourceException("Lỗi: Tên tiêu chí đánh giá đã tồn tại ở một bản ghi khác!");
        }

        evaluationCriteriaMapper.updateEntityFromDto(dto, criteria);
        criteria.setUpdatedAt(LocalDateTime.now());
        EvaluationCriteria updatedEntity = evaluationCriteriaRepository.save(criteria);

        return evaluationCriteriaMapper.toResponse(updatedEntity);
    }

    @Override
    @Transactional
    public void deleteCriteria(Long id) {
        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: Không tìm thấy tiêu chí đánh giá với id: " + id)
        );


        // 1. Check xem tiêu chí đã được đưa vào đợt đánh giá nào chưa
        if (roundCriteriaRepository.existsByCriterion_CriterionId(id)) {
            throw new BadRequestException("Lỗi: Không thể xóa tiêu chí này vì đang được cấu hình trong một đợt đánh giá!");
        }

        // 2. Check xem tiêu chí này đã từng được chấm điểm cho bất kỳ sinh viên nào chưa
        if (assessmentResultRepository.existsByCriterion_CriterionId(id)) {
            throw new BadRequestException("Lỗi: Không thể xóa tiêu chí này vì đã có dữ liệu chấm điểm của sinh viên!");
        }

        // Cho phép xóa
        evaluationCriteriaRepository.delete(criteria);
    }
}
