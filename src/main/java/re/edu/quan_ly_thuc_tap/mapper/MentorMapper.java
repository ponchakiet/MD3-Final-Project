package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.MentorCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.MentorUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.MentorPublicResponse;
import re.edu.quan_ly_thuc_tap.dto.response.MentorResponse;
import re.edu.quan_ly_thuc_tap.entity.Mentor;
import re.edu.quan_ly_thuc_tap.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MentorMapper {

    // Mapping sang bản đầy đủ
    @Mapping(source = "user.userId", target = "userId")
    @Mapping(source = "user.userName", target = "userName")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    @Mapping(source = "user.isActive", target = "isActive")
    @Mapping(source = "user.createdAt", target = "createdAt")
    @Mapping(source = "user.updatedAt", target = "updatedAt")
    MentorResponse toMentorResponse(Mentor mentor);

    // Mapping sang bản rút gọn dành cho Sinh viên
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    MentorPublicResponse toMentorPublicResponse(Mentor mentor);

    // DTO tạo mới  --> Mentor
    @Mapping(target = "user", ignore = true)
    Mentor toMentor(MentorCreateRequestDTO dto);

    //DTO tạo mới  --> User
    @Mapping(target = "userName", source = "userName")
    @Mapping(target = "passwordHash", ignore = true) // Service thực hiện
    User toUser(MentorCreateRequestDTO dto);

    // Cập nhật thông tin Mentor (Bỏ qua trường null để giữ data cũ)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateMentorFromDto(MentorUpdateRequestDTO dto, @MappingTarget Mentor mentor);

    // Cập nhật thông tin User cơ bản (Bỏ qua trường null)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(MentorUpdateRequestDTO dto, @MappingTarget User user);
}