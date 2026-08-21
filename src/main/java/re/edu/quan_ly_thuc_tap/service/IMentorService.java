package re.edu.quan_ly_thuc_tap.service;

import org.springframework.data.domain.Pageable;
import re.edu.quan_ly_thuc_tap.dto.request.MentorCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.MentorUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.MentorResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

public interface IMentorService {
    PageResponse<?> getAllMentor(String keyword, Long userId, RoleEnum role, Pageable pageable);
    Object findMentorById(Long mentorId, Long userId, RoleEnum role);
    MentorResponse createMentor(MentorCreateRequestDTO dto);
    MentorResponse updateMentor(Long mentorId, MentorUpdateRequestDTO dto, Long currentUserId, RoleEnum role);
}
