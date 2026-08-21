package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.EvaluationCriteria;

@Repository
public interface IEvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Long> {

    @Query("""
    SELECT e FROM EvaluationCriteria e
    WHERE LOWER(e.criterionName) LIKE LOWER(:keyword)
    """)
    Page<EvaluationCriteria> findAllCriteria(@Param("keyword") String keyword, Pageable pageable);

    Boolean existsByCriterionName(String criterionName);

    // Dùng để check trùng tên khi Update (bỏ qua chính nó)
    Boolean existsByCriterionNameAndCriterionIdNot(String criterionName, Long criterionId);
}