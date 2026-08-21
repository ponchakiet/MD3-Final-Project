package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.InternshipPhase;

@Repository
public interface IInternshipPhaseRepository extends JpaRepository<InternshipPhase, Long> {
    boolean existsByPhaseName(String phaseName);
}
