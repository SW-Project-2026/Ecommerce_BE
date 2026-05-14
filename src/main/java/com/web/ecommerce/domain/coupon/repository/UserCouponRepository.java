package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

  Page<UserCoupon> findByUserId(Long userId, Pageable pageable);

  boolean existsByUserIdAndCouponId(Long userId, Long couponId);

  long countByCouponId(Long couponId);
}
