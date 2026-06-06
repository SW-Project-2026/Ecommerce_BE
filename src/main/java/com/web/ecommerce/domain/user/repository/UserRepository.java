package com.web.ecommerce.domain.user.repository;

import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.entity.UserGrade;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginIdAndIsActive(String loginId, int isActive);

  Optional<User> findByIdAndIsActive(Long id, int isActive);

  boolean existsByLoginId(String loginId);

  boolean existsByLoginIdAndIsActive(String loginId, int isActive);

  boolean existsByEmail(String email);

  boolean existsByEmailAndIsActive(String email, int isActive);

  Page<User> findAllByRole(Role role, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.isActive = 1 AND u.role = 'USER' AND u.phone IS NOT NULL AND u.phone <> '' AND u.marketingAgreed = true")
  List<User> findSmsTargetUsers();

  @Query("SELECT u FROM User u WHERE u.isActive = 1 AND u.role = 'USER' AND u.phone IS NOT NULL AND u.phone <> '' AND u.marketingAgreed = true AND u.createdAt >= :since")
  List<User> findNewSmsTargetUsers(@Param("since") LocalDateTime since);

  long countByRoleAndIsActive(Role role, int isActive);

  long countByRoleAndIsActiveAndCreatedAtAfter(Role role, int isActive, LocalDateTime after);

  long countByRoleAndIsActiveAndGrade(Role role, int isActive, UserGrade grade);

  @Query(value = "SELECT TO_CHAR(created_at, 'YYYY-MM') AS month, COUNT(*) AS count FROM users WHERE role = 'USER' AND created_at >= :from GROUP BY TO_CHAR(created_at, 'YYYY-MM') ORDER BY month", nativeQuery = true)
  List<Object[]> countMonthlySignups(@Param("from") LocalDateTime from);

  @Query(value = """
          SELECT * FROM users
          WHERE role = 'USER' AND is_active = 1
          AND (:search = '' OR login_id LIKE CONCAT('%', :search, '%'))
          AND (CAST(:grade AS varchar) IS NULL OR grade = CAST(:grade AS varchar))
          AND (CAST(:loginBefore AS timestamp) IS NULL OR last_login_at IS NULL OR last_login_at < CAST(:loginBefore AS timestamp))
          AND (CAST(:loginAfter AS timestamp) IS NULL OR (last_login_at IS NOT NULL AND last_login_at >= CAST(:loginAfter AS timestamp)))
          AND (CAST(:createdAfter AS timestamp) IS NULL OR created_at >= CAST(:createdAfter AS timestamp))
          """,
         countQuery = """
          SELECT COUNT(*) FROM users
          WHERE role = 'USER' AND is_active = 1
          AND (:search = '' OR login_id LIKE CONCAT('%', :search, '%'))
          AND (CAST(:grade AS varchar) IS NULL OR grade = CAST(:grade AS varchar))
          AND (CAST(:loginBefore AS timestamp) IS NULL OR last_login_at IS NULL OR last_login_at < CAST(:loginBefore AS timestamp))
          AND (CAST(:loginAfter AS timestamp) IS NULL OR (last_login_at IS NOT NULL AND last_login_at >= CAST(:loginAfter AS timestamp)))
          AND (CAST(:createdAfter AS timestamp) IS NULL OR created_at >= CAST(:createdAfter AS timestamp))
          """,
         nativeQuery = true)
  Page<User> findCustomersWithFilter(
          @Param("search") String search,
          @Param("grade") String grade,
          @Param("loginBefore") LocalDateTime loginBefore,
          @Param("loginAfter") LocalDateTime loginAfter,
          @Param("createdAfter") LocalDateTime createdAfter,
          Pageable pageable
  );

  @Query(value = """
          SELECT * FROM users
          WHERE role = 'USER' AND is_active = 1
          AND (:search = '' OR login_id LIKE CONCAT('%', :search, '%'))
          AND id IN :includeIds
          """,
         countQuery = """
          SELECT COUNT(*) FROM users
          WHERE role = 'USER' AND is_active = 1
          AND (:search = '' OR login_id LIKE CONCAT('%', :search, '%'))
          AND id IN :includeIds
          """,
         nativeQuery = true)
  Page<User> findCustomersByIds(
          @Param("search") String search,
          @Param("includeIds") List<Long> includeIds,
          Pageable pageable);

  @Query(value = "SELECT TO_CHAR(updated_at, 'YYYY-MM') AS month, COUNT(*) AS withdrawn, (SELECT COUNT(*) FROM users u2 WHERE u2.role = 'USER' AND TO_CHAR(u2.created_at, 'YYYY-MM') = TO_CHAR(u.updated_at, 'YYYY-MM')) AS total FROM users u WHERE u.role = 'USER' AND u.is_active = 0 AND u.updated_at >= :from GROUP BY TO_CHAR(u.updated_at, 'YYYY-MM') ORDER BY month", nativeQuery = true)
  List<Object[]> countMonthlyChurn(@Param("from") LocalDateTime from);
}
