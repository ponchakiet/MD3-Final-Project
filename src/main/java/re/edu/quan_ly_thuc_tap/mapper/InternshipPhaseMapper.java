package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipPhaseUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipPhaseResponse;
import re.edu.quan_ly_thuc_tap.entity.InternshipPhase;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InternshipPhaseMapper {

    // Entity -> Response
    InternshipPhaseResponse toResponse(InternshipPhase entity);

    // DTO tạo mới -> Entity
    InternshipPhase toEntity(InternshipPhaseCreateRequestDTO dto);

    // Cập nhật Entity từ DTO (giữ nguyên field cũ nếu request truyền lên null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDto(InternshipPhaseUpdateRequestDTO dto, @MappingTarget InternshipPhase entity);
}
