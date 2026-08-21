package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.AssessmentRoundUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.AssessmentRoundResponse;
import re.edu.quan_ly_thuc_tap.entity.AssessmentRound;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AssessmentRoundMapper {

    // map Phase ở tầng Service
    @Mapping(target = "phase", ignore = true)
    AssessmentRound toEntity(AssessmentRoundCreateRequestDTO dto);

    // Entity -> Response
    @Mapping(target = "phaseId", source = "phase.phaseId")
    @Mapping(target = "phaseName", source = "phase.phaseName")
    AssessmentRoundResponse toResponse(AssessmentRound entity);

    @Mapping(target = "phase", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(AssessmentRoundUpdateRequestDTO dto, @MappingTarget AssessmentRound entity);
}
