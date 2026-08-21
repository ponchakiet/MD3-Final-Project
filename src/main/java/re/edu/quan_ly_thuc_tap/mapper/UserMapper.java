package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.UserCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.UserUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    // Entity → UserResponse.
    UserResponse toUserResponse(User user);

    // UserCreateRequestDTO →  Entity
    //   - userId       : DB tự generate (IDENTITY)
    //   - passwordHash : cần BCrypt encode từ request.getPassword()
    //   - refreshToken : null lúc mới tạo
    //   - isActive     : default true, không cần set
    //   - createdAt / updatedAt : tự động set
    @Mapping(target = "userId",        ignore = true)
    @Mapping(target = "passwordHash",  ignore = true)
    @Mapping(target = "refreshToken",  ignore = true)
    @Mapping(target = "isActive",      ignore = true)
    @Mapping(target = "createdAt",     ignore = true)
    @Mapping(target = "updatedAt",     ignore = true)
    User toEntity(UserCreateRequestDTO request);

    // UserUpdateRequestDTO → patch lên Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromDTO(UserUpdateRequestDTO request, @MappingTarget User user);

}
