package com.web.ecommerce.domain.coupon.service;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import java.util.List;

public interface CouponService {

  CouponResponse createCoupon(CreateCouponRequest request);

  List<CouponResponse> getCoupons();

  CouponResponse getCoupon(Long couponId);

  CouponResponse updateCoupon(Long couponId, UpdateCouponRequest request);

  void deleteCoupon(Long couponId);

  UserCouponResponse issueCoupon(Long couponId, Long userId);

  List<UserCouponResponse> getUserCoupons(Long userId);

  UserCouponResponse useCoupon(Long userCouponId);
}
