package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.entity.Mentor;
import re.edu.quan_ly_thuc_tap.entity.Student;

import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Long> {
    @Query("""
    SELECT s FROM Student s
    JOIN FETCH s.user u
    WHERE LOWER(u.fullName) LIKE LOWER(:keyword)
    AND (:mentorId IS NULL OR EXISTS (
        SELECT 1 FROM InternshipAssignment ia 
        WHERE ia.student = s AND ia.mentor.mentorId = :mentorId
    ))
""")
    Page<Student> findAll(
            @Param("keyword") String keyword,
            @Param("mentorId") Long mentorId,
            Pageable pageable
    );

    Optional<Student> findStudentByStudentId(Long studentId);
    Boolean existsByStudentCode(String studentCode);
}
