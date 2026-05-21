package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

  Page<UserCoupon> findByUserIdAndStatusAndIsDuplicateFalse(Long userId, CouponStatus status, Pageable pageable);

  boolean existsByUserIdAndCouponId(Long userId, Long couponId);

  boolean existsByUserIdAndCouponIdAndIsDuplicateFalseAndCreatedAtAfter(Long userId, Long couponId, LocalDateTime after);

  long countByCouponIdAndIsDuplicateFalse(Long couponId);

  @Query("SELECT uc FROM UserCoupon uc WHERE uc.status = 'AVAILABLE' AND uc.isDuplicate = false " +
      "AND uc.coupon.expiredAt IS NOT NULL")
  List<UserCoupon> findAvailableWithExpiry();
}
