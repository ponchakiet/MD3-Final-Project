package re.edu.quan_ly_thuc_tap.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import re.edu.quan_ly_thuc_tap.config.security.UserDetailsCustom;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentCreateRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.request.InternshipAssignmentUpdateStatusRequestDTO;
import re.edu.quan_ly_thuc_tap.dto.response.InternshipAssignmentResponse;
import re.edu.quan_ly_thuc_tap.dto.response.pagination.PageResponse;
import re.edu.quan_ly_thuc_tap.entity.*;
import re.edu.quan_ly_thuc_tap.exception.DuplicateResourceException;
import re.edu.quan_ly_thuc_tap.exception.ResourceNotFoundException;
import re.edu.quan_ly_thuc_tap.mapper.InternshipAssignmentMapper;
import re.edu.quan_ly_thuc_tap.repository.IInternshipAssignmentRepository;
import re.edu.quan_ly_thuc_tap.repository.IInternshipPhaseRepository;
import re.edu.quan_ly_thuc_tap.repository.IMentorRepository;
import re.edu.quan_ly_thuc_tap.repository.IStudentRepository;
import re.edu.quan_ly_thuc_tap.service.IInternshipAssignmentService;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;
import re.edu.quan_ly_thuc_tap.util.helper.PageResponseHelper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements IInternshipAssignmentService {

    private final InternshipAssignmentMapper assignmentMapper;
    private final IInternshipAssignmentRepository assignmentRepository;
    private final IStudentRepository studentRepository;
    private final IMentorRepository mentorRepository;
    private final IInternshipPhaseRepository phaseRepository;

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
        return userDetails.getUser();
    }

    // ─── GET ALL ─────────────────────────────────────────────────────────────
    @Override
    public PageResponse<InternshipAssignmentResponse> getAllAssignments(
            Long userId, RoleEnum role, Pageable pageable) {

        Long studentId = null;
        Long mentorId  = null;

        if (role == RoleEnum.STUDENT) {
            studentId = userId;
        } else if (role == RoleEnum.MENTOR) {
            mentorId = userId;
        }
        // ADMIN: cả 2 null → lấy tất cả

        Page<InternshipAssignment> page = assignmentRepository.findAllByFilter(
                studentId, mentorId, pageable
        );

        return PageResponseHelper.toPageResponse(page.map(assignmentMapper::toResponse));
    }

    // ─── GET BY ID ────────────────────────────────────────────────────────────
    @Override
    public InternshipAssignmentResponse getAssignmentById(
            Long assignmentId, Long userId, RoleEnum role) {

        InternshipAssignment assignment = assignmentRepository.findByIdWithDetails(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy phân công thực tập với id: " + assignmentId));

        if (role == RoleEnum.MENTOR) {
            if (!assignment.getMentor().getMentorId().equals(userId)) {
                throw new AccessDeniedException("Lỗi: Bạn không có quyền xem phân công này!");
            }
        }

        if (role == RoleEnum.STUDENT) {
            if (!assignment.getStudent().getStudentId().equals(userId)) {
                throw new AccessDeniedException("Lỗi: Bạn không có quyền xem phân công này!");
            }
        }

        return assignmentMapper.toResponse(assignment);
    }

    // ─── CREATE ──────────────────────────────────────────────────────────────
    @Override
    @Transactional
    public InternshipAssignmentResponse createAssignment(InternshipAssignmentCreateRequestDTO dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy sinh viên với id: " + dto.getStudentId()));

        Mentor mentor = mentorRepository.findById(dto.getMentorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy giáo viên hướng dẫn với id: " + dto.getMentorId()));

        InternshipPhase phase = phaseRepository.findById(dto.getPhaseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Không tìm thấy giai đoạn thực tập với id: " + dto.getPhaseId()));

        // Mỗi sinh viên chỉ được phân công 1 lần trong 1 giai đoạn
        if (assignmentRepository.existsByStudent_StudentIdAndPhase_PhaseId(
                dto.getStudentId(), dto.getPhaseId())) {
            throw new DuplicateResourceException(
                    "Lỗi: Sinh viên này đã được phân công trong giai đoạn thực tập đó rồi!");
        }

        InternshipAssignment assignment = InternshipAssignment.builder()
                .student(student)
                .mentor(mentor)
                .phase(phase)
                .build();

        InternshipAssignment saved = assignmentRepository.save(assignment);

        // Fetch lại với JOIN FETCH để tránh n+1
        return assignmentMapper.toResponse(
                assignmentRepository.findByIdWithDetails(saved.getAssignmentId()).orElseThrow()
        );
    }

    // ─── UPDATE STATUS ────────────────────────────────────────────────────────
    @Override
    @Transactional
    public InternshipAssignmentResponse updateStatus(
            Long assignmentId,
            InternshipAssignmentUpdateStatusRequestDTO dto
    ) {
        InternshipAssignment assignment = assignmentRepository.findByIdWithDetails(assignmentId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Lỗi: Không tìm thấy phân công thực tập với id: " + assignmentId));

        assignment.setStatus(dto.getStatus());
        assignment.setUpdatedAt(LocalDateTime.now());
        assignmentRepository.save(assignment);

        return assignmentMapper.toResponse(assignment);
    }
}