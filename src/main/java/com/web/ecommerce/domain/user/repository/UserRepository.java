package com.web.ecommerce.domain.user.repository;

import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
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

  boolean existsByEmail(String email);

  Page<User> findAllByRole(Role role, Pageable pageable);

  @Query("SELECT u FROM User u WHERE u.isActive = 1 AND u.role = 'USER' AND u.phone IS NOT NULL AND u.phone <> '' AND u.marketingAgreed = true")
  List<User> findSmsTargetUsers();

  @Query("SELECT u FROM User u WHERE u.isActive = 1 AND u.role = 'USER' AND u.phone IS NOT NULL AND u.phone <> '' AND u.marketingAgreed = true AND u.createdAt >= :since")
  List<User> findNewSmsTargetUsers(@Param("since") LocalDateTime since);
}
