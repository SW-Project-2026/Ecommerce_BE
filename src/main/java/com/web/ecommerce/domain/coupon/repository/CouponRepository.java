package com.web.ecommerce.domain.coupon.repository;

import com.web.ecommerce.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

  boolean existsByCode(String code);
}
