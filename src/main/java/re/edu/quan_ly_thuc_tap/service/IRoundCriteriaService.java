package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.RoundCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;

public interface IRoundCriteriaService {
    PageResponse<RoundCriteriaResponse> getAllCriteriaInRound(Long roundId, Pageable pageable);

    RoundCriteriaResponse getRoundCriterionById(Long roundCriterionId);

    RoundCriteriaResponse addCriterionToRound(RoundCriteriaCreateRequestDTO dto);

    RoundCriteriaResponse updateCriterionWeightInRound(Long roundCriterionId, RoundCriteriaUpdateRequestDTO dto);

    void removeCriterionFromRound(Long roundCriterionId);
}