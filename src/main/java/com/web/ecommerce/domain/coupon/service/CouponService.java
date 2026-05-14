package com.web.ecommerce.domain.coupon.service;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CouponService {

  CouponResponse createCoupon(CreateCouponRequest request);

  Page<CouponResponse> getCoupons(Pageable pageable);

  CouponResponse getCoupon(Long couponId);

  CouponResponse updateCoupon(Long couponId, UpdateCouponRequest request);

  void deleteCoupon(Long couponId);

  UserCouponResponse issueCoupon(Long couponId, Long userId);

  Page<UserCouponResponse> getUserCoupons(Long userId, Pageable pageable);

  UserCouponResponse useCoupon(Long userCouponId);
}
