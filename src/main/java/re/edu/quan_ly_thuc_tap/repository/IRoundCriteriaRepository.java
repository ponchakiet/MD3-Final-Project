package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.RoundCriteria;

import java.math.BigDecimal;

@Repository
public interface IRoundCriteriaRepository extends JpaRepository<RoundCriteria, Long> {

    // Kiểm tra xem tiêu chí này đã được sử dụng trong bất kỳ đợt đánh giá nào chưa
    boolean existsByCriterion_CriterionId(Long criterionId);
    // Check cặ[ unique (roundId, CriteriaId)
    boolean existsByRound_RoundIdAndCriterion_CriterionId(Long roundId, Long criterionId);
    void deleteAllByRound_RoundId(Long roundId);

    @Query("""
        SELECT rc FROM RoundCriteria rc
        JOIN FETCH rc.round r
        JOIN FETCH rc.criterion c
        WHERE r.roundId = :roundId
    """)
    Page<RoundCriteria> findAllByRoundId(
            @Param("roundId") Long roundId,
            Pageable pageable);

    @Query("""
    select sum(rc.weight)
    from RoundCriteria rc
    where rc.round.roundId = :roundId
    """)
    BigDecimal sumWeightByRoundId(@Param("roundId") Long roundId);
}
