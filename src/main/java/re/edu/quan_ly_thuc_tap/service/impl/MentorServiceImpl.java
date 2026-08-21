package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.MentorCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.MentorUpdateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.MentorResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.Mentor;
import re.edu.quan_ly_thuc_tap.entity.Student;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.exception.BadRequestException;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.MentorMapper;
import re.edu.quan_ly_thuc_tap.repository.IInternshipAssignmentRepository;
import re.edu.quan_ly_thuc_tap.repository.IMentorRepository;
import re.edu.quan_ly_thuc_tap.repository.IUserRepository;
import re.edu.quan_ly_thuc_tap.service.IMentorService;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements IMentorService {
    private final MentorMapper mentorMapper;
    private final PasswordEncoder passwordEncoder;
    private final IUserRepository userRepository;
    private final IMentorRepository mentorRepository;
    private final IInternshipAssignmentRepository internshipAssignmentRepository;

    @Override
    public PageResponse<?> getAllMentor(String keyword, Long userId, RoleEnum role, Pageable pageable) {
        String searchKeyword = StringUtils.hasText(keyword) ? "%" + keyword.trim() + "%" : "%%";

        Page<Mentor> page = mentorRepository.findAllMentors(
                searchKeyword,
                role == RoleEnum.STUDENT ? userId : null,
                pageable
        );

        if (role == RoleEnum.STUDENT) {
            return PageResponseHelper.toPageResponse(page.map(mentorMapper::toMentorPublicResponse));
        }

        return PageResponseHelper.toPageResponse(page.map(mentorMapper::toMentorResponse));
    }

    @Override
    public Object findMentorById(Long mentorId, Long userId, RoleEnum role) {
        Mentor m = mentorRepository.findById(mentorId).orElseThrow(
                () -> new ResourceNotFoundException("Lỗi: không tìm thấy mentor với id: " + mentorId)
        );

        if (role == RoleEnum.STUDENT) {
            boolean isAssigned = internshipAssignmentRepository.existsByStudent_StudentIdAndMentor_MentorId(
                    userId, mentorId
            );
            if (!isAssigned) {
                throw new AccessDeniedException("Lỗi: Bạn không được phân công cho giáo viên hướng dẫn này!");
            }
            return mentorMapper.toMentorPublicResponse(m);
        }

        if (role == RoleEnum.MENTOR) {
            if (!mentorId.equals(userId)) {
                throw new AccessDeniedException("Lỗi: Bạn không có quyền xem thông tin của mentor khác!");
            }
        }

        return mentorMapper.toMentorResponse(m);
    }

    @Override
    @Transactional
    public MentorResponse createMentor(MentorCreateRequestDTO dto) {
        if (userRepository.existsByUserName(dto.getUserName())) {
            throw new DuplicateResourceException("Lỗi: Tên đăng nhập đã tồn tại!");
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new DuplicateResourceException("Lỗi: Email đã tồn tại!");
        }

        User user = mentorMapper.toUser(dto);
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
        user.setRole(RoleEnum.MENTOR);
        user.setIsActive(true);
        User savedUser = userRepository.save(user);

        Mentor mentor = mentorMapper.toMentor(dto);
        mentor.setUser(savedUser);
        Mentor savedMentor = mentorRepository.save(mentor);

        return mentorMapper.toMentorResponse(savedMentor);
    }

    @Override
    @Transactional
    public MentorResponse updateMentor(Long mentorId, MentorUpdateRequestDTO dto,
                                       Long currentUserId, RoleEnum role) {
        Mentor mentor = mentorRepository.findById(mentorId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy giáo viên hướng dẫn với id: " + mentorId));

        if (role == RoleEnum.MENTOR) {
            if (!mentorId.equals(currentUserId)) {
                throw new AccessDeniedException("Lỗi: Bạn không có quyền cập nhật thông tin của giáo viên khác!");
            }
        }

        if (!dto.getEmail().equals(mentor.getUser().getEmail())) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new BadRequestException("Lỗi: Email đã được sử dụng bởi người dùng khác!");
            }
        }

        mentorMapper.updateUserFromDto(dto, mentor.getUser());
        mentorMapper.updateMentorFromDto(dto, mentor);
        userRepository.save(mentor.getUser());
        mentor.setUpdatedAt(LocalDateTime.now());
        Mentor updatedMentor = mentorRepository.save(mentor);

        return mentorMapper.toMentorResponse(updatedMentor);
    }
}
