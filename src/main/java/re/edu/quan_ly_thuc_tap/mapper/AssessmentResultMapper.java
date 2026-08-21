package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentResultUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentResultResponse;
import re.edu.quan_ly_thuc_tap.entity.AssessmentResult;
import re.edu.quan_ly_thuc_tap.entity.AssessmentRound;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;
import re.edu.quan_ly_thuc_tap.entity.InternshipAssignment;
import re.edu.quan_ly_thuc_tap.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssessmentResultMapper {

    // ─── Entity -> Response ───────────────────────────────────────────────────
    @Mapping(target = "assignment",  source = "assignment")
    @Mapping(target = "round",       source = "round")
    @Mapping(target = "criterion",   source = "criterion")
    @Mapping(target = "evaluatedBy", source = "evaluatedBy")
    AssessmentResultResponse toResponse(AssessmentResult entity);

    // ─── InternshipAssignment -> AssignmentSummary ────────────────────────────
    @Mapping(target = "studentCode", source = "student.studentCode")
    @Mapping(target = "studentName", source = "student.user.fullName")
    @Mapping(target = "mentorName",  source = "mentor.user.fullName")
    @Mapping(target = "phaseName",   source = "phase.phaseName")
    AssessmentResultResponse.AssignmentSummary toAssignmentSummary(InternshipAssignment assignment);

    // ─── AssessmentRound -> RoundSummary ─────────────────────────────────────
    // Các field khớp (không cần @Mapping)
    AssessmentResultResponse.RoundSummary toRoundSummary(AssessmentRound round);

    // ─── EvaluationCriteria -> CriterionSummary ──────────────────────────────
    AssessmentResultResponse.CriterionSummary toCriterionSummary(EvaluationCriteria criterion);

    // ─── User -> EvaluatedBySummary ───────────────────────────────────────────
    AssessmentResultResponse.EvaluatedBySummary toEvaluatedBySummary(User user);

    // ─── Update entity từ DTO (chỉ score và comments) ────────────────────────
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssessmentResultUpdateRequestDTO dto,
                             @MappingTarget AssessmentResult entity);
}