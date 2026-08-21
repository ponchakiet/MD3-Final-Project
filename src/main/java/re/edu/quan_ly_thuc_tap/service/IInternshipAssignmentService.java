package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentUpdateStatusRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipAssignmentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

public interface IInternshipAssignmentService {
    PageResponse<InternshipAssignmentResponse> getAllAssignments(Long userId, RoleEnum role, Pageable pageable);
    InternshipAssignmentResponse getAssignmentById(Long assignmentId, Long userId, RoleEnum role);
    InternshipAssignmentResponse createAssignment(InternshipAssignmentCreateRequestDTO dto);
    InternshipAssignmentResponse updateStatus(Long assignmentId, InternshipAssignmentUpdateStatusRequestDTO dto);
}