package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.InternshipAssignment;

import java.util.Optional;

@Repository
public interface IInternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {
    @Query(value = """
    select i
    from InternshipAssignment i
    join fetch i.student s
    join fetch s.user
    join fetch i.mentor m
    join fetch m.user
    join fetch i.phase
    where (i.student.studentId = :studentId OR :studentId IS NULL)
    and   (i.mentor.mentorId   = :mentorId  OR :mentorId  IS NULL)
    """)
    Page<InternshipAssignment> findAllByFilter(
            @Param("studentId") Long studentId,
            @Param("mentorId")  Long mentorId,
            Pageable pageable
    );

    @Query(value = """
    select i
    from InternshipAssignment i
    join fetch i.student s
    join fetch s.user
    join fetch i.mentor m
    join fetch m.user
    join fetch i.phase
    where i.assignmentId = :assignmentId
    """)
    Optional<InternshipAssignment> findByIdWithDetails(
            @Param("assignmentId") Long assignmentId
    );
    boolean existsByPhase_PhaseId(Long phaseId);
    Boolean existsByStudent_StudentIdAndMentor_MentorId(Long studentStudentId, Long mentorMentorId);
    boolean existsByStudent_StudentIdAndPhase_PhaseId(Long studentId, Long phaseId);
    boolean existsByMentor_MentorId(Long mentorId);
    boolean existsByStudent_StudentId(Long studentId);
}
