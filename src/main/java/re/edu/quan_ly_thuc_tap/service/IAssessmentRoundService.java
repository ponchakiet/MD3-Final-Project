package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentRoundResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;

public interface IAssessmentRoundService {
    PageResponse<AssessmentRoundResponse> getAllRounds(Long phaseId, String keyword, Pageable pageable);

    AssessmentRoundResponse getRoundById(Long id);

    AssessmentRoundResponse createRound(AssessmentRoundCreateRequestDTO dto);

    AssessmentRoundResponse updateRound(Long id, AssessmentRoundUpdateRequestDTO dto);

    void deleteRound(Long id);
}