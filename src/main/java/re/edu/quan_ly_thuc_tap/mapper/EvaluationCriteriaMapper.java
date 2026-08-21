package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.EvaluationCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.EvaluationCriteriaResponse;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EvaluationCriteriaMapper {

    // CreateDTO -> Entity
    EvaluationCriteria toEntity(EvaluationCriteriaCreateRequestDTO dto);

    // Entity -> Response
    EvaluationCriteriaResponse toResponse(EvaluationCriteria entity);

    // Cập nhật Entity từ UpdateDTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(EvaluationCriteriaUpdateRequestDTO dto, @MappingTarget EvaluationCriteria entity);
}