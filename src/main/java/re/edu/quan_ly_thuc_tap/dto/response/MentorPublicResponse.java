package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MentorPublicResponse {
    private Long mentorId;
    private String fullName;
    private String email;
    private String department;
    private String academicRank;
}
