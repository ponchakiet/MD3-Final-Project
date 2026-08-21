package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaCreateRequestDTO;

import re.edu.quan_ly_thuc_tap.dto.response.RoundCriteriaResponse;
import re.edu.quan_ly_thuc_tap.dto.request.RoundCriteriaUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.entity.RoundCriteria;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoundCriteriaMapper {

    // Không map Round và Criterion
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "criterion", ignore = true)
    RoundCriteria toEntity(RoundCriteriaCreateRequestDTO dto);

    // Bóc tách dữ liệu từ các object con (Round và Criterion) ra Response
    @Mapping(target = "roundId", source = "round.roundId")
    @Mapping(target = "roundName", source = "round.roundName")
    @Mapping(target = "criterionId", source = "criterion.criterionId")
    @Mapping(target = "criterionName", source = "criterion.criterionName")
    @Mapping(target = "maxScore", source = "criterion.maxScore")
    RoundCriteriaResponse toResponse(RoundCriteria entity);

    // Cập nhật Entity từ UpdateDTO (chỉ cập nhật weight)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(RoundCriteriaUpdateRequestDTO dto, @MappingTarget RoundCriteria entity);

}