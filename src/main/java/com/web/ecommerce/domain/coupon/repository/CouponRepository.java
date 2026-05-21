package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.Coupon;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  boolean existsByCode(String code);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("SELECT c FROM Coupon c WHERE c.id = :id")
  Optional<Coupon> findByIdWithLock(Long id);

  @Query("SELECT c FROM Coupon c WHERE c.id > :cursor ORDER BY c.id ASC")
  List<Coupon> findByCursor(Long cursor, Pageable pageable);
}
