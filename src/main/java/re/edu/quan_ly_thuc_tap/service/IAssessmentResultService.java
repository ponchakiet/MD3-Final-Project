package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentResultResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

public interface IAssessmentResultService {

    PageResponse<AssessmentResultResponse> getAllResults(
            Long userId, RoleEnum role, Long assignmentId, Pageable pageable);

    AssessmentResultResponse createResult(
            AssessmentResultCreateRequestDTO dto, User currentUser);

    AssessmentResultResponse updateResult(
            Long resultId, AssessmentResultUpdateRequestDTO dto, User currentUser);
}