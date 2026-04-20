package com.web.ecommerce.domain.user.repository;

import com.web.ecommerce.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByLoginId(Long loginId, int isActive);
}
