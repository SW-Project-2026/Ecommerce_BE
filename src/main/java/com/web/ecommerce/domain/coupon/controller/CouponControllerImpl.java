package com.web.ecommerce.domain.coupon.controller;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.service.CouponService;
import com.web.ecommerce.global.response.BaseResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CouponControllerImpl implements CouponController {

  private final CouponService couponService;

  @Override
  @PostMapping("/api/coupons")
  public ResponseEntity<BaseResponse<CouponResponse>> createCoupon(@RequestBody CreateCouponRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "쿠폰이 생성되었습니다.", couponService.createCoupon(request)));
  }

  @Override
  @GetMapping("/api/coupons")
  public ResponseEntity<BaseResponse<List<CouponResponse>>> getCoupons() {
    return ResponseEntity.ok(BaseResponse.success(couponService.getCoupons()));
  }

  @Override
  @GetMapping("/api/coupons/{couponId}")
  public ResponseEntity<BaseResponse<CouponResponse>> getCoupon(@PathVariable Long couponId) {
    return ResponseEntity.ok(BaseResponse.success(couponService.getCoupon(couponId)));
  }

  @Override
  @PutMapping("/api/coupons/{couponId}")
  public ResponseEntity<BaseResponse<CouponResponse>> updateCoupon(@PathVariable Long couponId,
      @RequestBody UpdateCouponRequest request) {
    return ResponseEntity.ok(BaseResponse.success(couponService.updateCoupon(couponId, request)));
  }

  @Override
  @DeleteMapping("/api/coupons/{couponId}")
  public ResponseEntity<Void> deleteCoupon(@PathVariable Long couponId) {
    couponService.deleteCoupon(couponId);
    return ResponseEntity.noContent().build();
  }

  @Override
  @PostMapping("/api/coupons/{couponId}/issue/{userId}")
  public ResponseEntity<BaseResponse<UserCouponResponse>> issueCoupon(@PathVariable Long couponId,
      @PathVariable Long userId) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "쿠폰이 발급되었습니다.", couponService.issueCoupon(couponId, userId)));
  }

  @Override
  @GetMapping("/api/users/{userId}/coupons")
  public ResponseEntity<BaseResponse<List<UserCouponResponse>>> getUserCoupons(@PathVariable Long userId) {
    return ResponseEntity.ok(BaseResponse.success(couponService.getUserCoupons(userId)));
  }

  @Override
  @PatchMapping("/api/users/{userId}/coupons/{userCouponId}/use")
  public ResponseEntity<BaseResponse<UserCouponResponse>> useCoupon(@PathVariable Long userId,
      @PathVariable Long userCouponId) {
    return ResponseEntity.ok(BaseResponse.success(couponService.useCoupon(userCouponId)));
  }
}
