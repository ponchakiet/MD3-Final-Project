package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipAssignmentResponse;
import re.edu.quan_ly_thuc_tap.entity.InternshipAssignment;
import re.edu.quan_ly_thuc_tap.entity.InternshipPhase;
import re.edu.quan_ly_thuc_tap.entity.Mentor;
import re.edu.quan_ly_thuc_tap.entity.Student;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InternshipAssignmentMapper {

    // ─── Entity -> Response ───────────────────────────────────────────────────
    // MapStruct sẽ tự gọi các method toStudentSummary / toMentorSummary / toPhaseSummary
    @Mapping(target = "student", source = "student")
    @Mapping(target = "mentor",  source = "mentor")
    @Mapping(target = "phase",   source = "phase")
    InternshipAssignmentResponse toResponse(InternshipAssignment entity);

    // ─── Student -> StudentSummary ────────────────────────────────────────────
    @Mapping(target = "fullName",  source = "user.fullName")
    @Mapping(target = "email",     source = "user.email")
    @Mapping(target = "className", source = "className")
    InternshipAssignmentResponse.StudentSummary toStudentSummary(Student student);

    // ─── Mentor -> MentorSummary ──────────────────────────────────────────────
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email",    source = "user.email")
    InternshipAssignmentResponse.MentorSummary toMentorSummary(Mentor mentor);

    // ─── InternshipPhase -> PhaseSummary ─────────────────────────────────────
    InternshipAssignmentResponse.PhaseSummary toPhaseSummary(InternshipPhase phase);

    // ─── Update status ────────────────────────────────────────────────────────
    // Chỉ cập nhật field status, giữ nguyên các field khác
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStatus(
            @MappingTarget InternshipAssignment entity,
            re.edu.quan_ly_thuc_tap.util.enums.AssignmentStatusEnum status
    );
}