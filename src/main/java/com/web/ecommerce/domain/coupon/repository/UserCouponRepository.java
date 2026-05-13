package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {

  List<UserCoupon> findByUserId(Long userId);

  boolean existsByUserIdAndCouponId(Long userId, Long couponId);
}
