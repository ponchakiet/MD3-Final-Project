package re.edu.quan_ly_thuc_tap.mapper;

import org.mapstruct.*;
import re.edu.quan_ly_thuc_tap.dto.request.StudentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.StudentUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.StudentResponse;
import re.edu.quan_ly_thuc_tap.entity.Student;
import re.edu.quan_ly_thuc_tap.entity.User;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StudentMapper {

    // CreateDTO -> Entity
    @Mapping(target = "passwordHash", source = "password")
    User toUser(StudentCreateRequestDTO dto);
    Student toStudent(StudentCreateRequestDTO dto);


    // Cập nhật
    // User (fullName, email, phoneNumber)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(StudentUpdateRequestDTO dto, @MappingTarget User user);

    // Student (major, className, dateOfBirth, address)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStudentFromDto(StudentUpdateRequestDTO dto, @MappingTarget Student student);


    // Entity -> Response
    @Mapping(target = "userId", source = "user.userId")
    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(target = "role", source = "user.role")
    @Mapping(target = "isActive", source = "user.isActive")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    StudentResponse toStudentResponse(Student student);
}