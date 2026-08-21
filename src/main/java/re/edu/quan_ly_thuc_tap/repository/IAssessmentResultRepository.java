package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.AssessmentResult;

import java.util.Optional;

@Repository
public interface IAssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    @Query("""
        SELECT r FROM AssessmentResult r
        JOIN FETCH r.assignment a
        JOIN FETCH a.student s
        JOIN FETCH s.user
        JOIN FETCH a.mentor m
        JOIN FETCH m.user
        JOIN FETCH a.phase
        JOIN FETCH r.round
        JOIN FETCH r.criterion
        JOIN FETCH r.evaluatedBy
        WHERE (a.mentor.mentorId   = :mentorId   OR :mentorId   IS NULL)
        AND   (a.student.studentId = :studentId  OR :studentId  IS NULL)
        AND   (a.assignmentId      = :assignmentId OR :assignmentId IS NULL)
        """)
    Page<AssessmentResult> findAllByFilter(
            @Param("mentorId")      Long mentorId,
            @Param("studentId")     Long studentId,
            @Param("assignmentId")  Long assignmentId,
            Pageable pageable
    );

    @Query("""
        SELECT r FROM AssessmentResult r
        JOIN FETCH r.assignment a
        JOIN FETCH a.student s
        JOIN FETCH s.user
        JOIN FETCH a.mentor m
        JOIN FETCH m.user
        JOIN FETCH a.phase
        JOIN FETCH r.round
        JOIN FETCH r.criterion
        JOIN FETCH r.evaluatedBy
        WHERE r.resultId = :resultId
        """)
    Optional<AssessmentResult> findByIdWithDetails(
            @Param("resultId") Long resultId
    );

    boolean existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
            Long assignmentId, Long roundId, Long criterionId);



    // Kiểm tra xem tiêu chí đã được chấm điểm cho sinh viên nào chưa
    Boolean existsByCriterion_CriterionId(Long criterionId);

    Boolean existsByRound_RoundId(Long roundId);

    boolean existsByRound_RoundIdAndCriterion_CriterionId(Long roundId, Long criterionId);

}