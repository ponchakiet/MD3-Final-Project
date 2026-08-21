package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.EvaluationCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;

public interface IEvaluationCriteriaService {
    PageResponse<EvaluationCriteriaResponse> findAllCriteria(String keyword, Pageable pageable);

    EvaluationCriteriaResponse findCriteriaById(Long id);

    EvaluationCriteriaResponse createCriteria(EvaluationCriteriaCreateRequestDTO dto);

    EvaluationCriteriaResponse updateCriteria(Long id, EvaluationCriteriaUpdateRequestDTO dto);

    void deleteCriteria(Long id);
}