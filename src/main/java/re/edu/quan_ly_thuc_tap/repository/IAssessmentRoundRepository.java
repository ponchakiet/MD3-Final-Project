package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.AssessmentRound;

@Repository
public interface IAssessmentRoundRepository extends JpaRepository<AssessmentRound, Long> {

    @Query("""
        SELECT r FROM AssessmentRound r
        JOIN FETCH r.phase
        WHERE (:phaseId IS NULL OR r.phase.phaseId = :phaseId)
        AND LOWER(r.roundName) LIKE LOWER(:keyword)
    """)
    Page<AssessmentRound> findAllRounds(
            @Param("phaseId") Long phaseId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    boolean existsByPhase_PhaseId(Long phasePhaseId);
}
