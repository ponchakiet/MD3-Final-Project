package re.edu.quan_ly_thuc_tap.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import re.edu.quan_ly_thuc_tap.dto.response.UserResponse;
import re.edu.quan_ly_thuc_tap.entity.User;
import re.edu.quan_ly_thuc_tap.util.enums.RoleEnum;

import java.util.Optional;

@Repository
public interface IUserRepository  extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Page<User> findByRole(RoleEnum role, Pageable pageable);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phone);
}
