package re.edu.quan_ly_thuc_tap.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    // User
    private Long userId;
    private String userName;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String role;
    private Boolean isActive;

    // Student
    private String studentCode;
    private String major;
    private String className;
    private LocalDate dateOfBirth;
    private String address;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
