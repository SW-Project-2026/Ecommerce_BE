package com.web.ecommerce.domain.user.repository;

import com.web.ecommerce.domain.user.entity.Role;
import com.web.ecommerce.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByLoginIdAndIsActive(String loginId, int isActive);

  Optional<User> findByIdAndIsActive(Long id, int isActive);

  boolean existsByLoginId(String loginId);

  boolean existsByEmail(String email);

  Page<User> findAllByRole(Role role, Pageable pageable);
}
