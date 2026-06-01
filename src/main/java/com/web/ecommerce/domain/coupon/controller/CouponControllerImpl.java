package com.web.ecommerce.domain.coupon.controller;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.CouponSelectResponse;
import com.web.ecommerce.domain.coupon.dto.response.DownloadableCouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.service.CouponService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
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
  public ResponseEntity<BaseResponse<Page<CouponResponse>>> getCoupons(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    return ResponseEntity.ok(BaseResponse.success(couponService.getCoupons(PageRequest.of(page, size))));
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
  @GetMapping("/api/users/me/coupons")
  public ResponseEntity<BaseResponse<Page<UserCouponResponse>>> getUserCoupons(
      @RequestParam String status,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "20") int size) {
    Long currentUserId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
    return ResponseEntity.ok(BaseResponse.success(couponService.getUserCoupons(currentUserId, status, PageRequest.of(page, size))));
  }

  @Override
  @PatchMapping("/api/users/me/coupons/{userCouponId}/use")
  public ResponseEntity<BaseResponse<UserCouponResponse>> useCoupon(@PathVariable Long userCouponId) {
    return ResponseEntity.ok(BaseResponse.success(couponService.useCoupon(userCouponId)));
  }

  @Override
  @GetMapping("/api/coupons/select")
  public ResponseEntity<BaseResponse<CursorResponse<CouponSelectResponse>>> getCouponSelectList(
      @RequestParam(required = false) Long cursor,
      @RequestParam(defaultValue = "3") int size) {
    return ResponseEntity.ok(BaseResponse.success(couponService.getCouponSelectList(cursor, size)));
  }

@Override
  @PostMapping("/api/coupons/{couponId}/download")
  public ResponseEntity<BaseResponse<UserCouponResponse>> downloadCoupon(@PathVariable Long couponId) {
    Long currentUserId = ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).id();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "쿠폰이 발급되었습니다.", couponService.downloadCoupon(couponId, currentUserId)));
  }

  @Override
  @GetMapping("/api/coupons/claim")
  public ResponseEntity<BaseResponse<UserCouponResponse>> claimCoupon(@RequestParam String token) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(BaseResponse.success(201, "쿠폰이 발급되었습니다.", couponService.claimCouponByToken(token)));
  }

}
