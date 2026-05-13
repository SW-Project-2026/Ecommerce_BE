package com.web.ecommerce.domain.coupon.controller;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.ResponseEntity;

@Tag(name = "Coupon", description = "쿠폰 API")
public interface CouponController {

  @Operation(summary = "쿠폰 생성 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> createCoupon(CreateCouponRequest request);

  @Operation(summary = "쿠폰 목록 조회 (ADMIN)")
  ResponseEntity<BaseResponse<List<CouponResponse>>> getCoupons();

  @Operation(summary = "쿠폰 단건 조회 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> getCoupon(Long couponId);

  @Operation(summary = "쿠폰 수정 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> updateCoupon(Long couponId, UpdateCouponRequest request);

  @Operation(summary = "쿠폰 삭제 (ADMIN)")
  ResponseEntity<Void> deleteCoupon(Long couponId);

  @Operation(summary = "특정 회원에게 쿠폰 발급 (ADMIN)")
  ResponseEntity<BaseResponse<UserCouponResponse>> issueCoupon(Long couponId, Long userId);

  @Operation(summary = "회원 쿠폰 목록 조회")
  ResponseEntity<BaseResponse<List<UserCouponResponse>>> getUserCoupons(Long userId);

  @Operation(summary = "쿠폰 사용 처리")
  ResponseEntity<BaseResponse<UserCouponResponse>> useCoupon(Long userId, Long userCouponId);
}
