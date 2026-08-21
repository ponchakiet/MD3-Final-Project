package re.edu.quan_ly_thuc_tap.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.Mentor;

import java.util.Optional;

@Repository
public interface IMentorRepository extends JpaRepository<Mentor, Long> {

    @Query("""
        SELECT m FROM Mentor m
        JOIN FETCH m.user u
        WHERE LOWER(u.fullName) LIKE LOWER(:keyword)
        AND (:studentId IS NULL OR EXISTS (
        SELECT 1 FROM InternshipAssignment ia
        WHERE (ia.student.studentId=:studentId AND ia.mentor = m)
    ))
    """)
    Page<Mentor> findAllMentors(
            @Param("keyword") String keyword,
            @Param("studentId") Long studentId,
            Pageable pageable
    );

    Optional<Mentor> findByMentorId(Long mentorId);

    boolean existsByMentorId(Long mentorId);


}
