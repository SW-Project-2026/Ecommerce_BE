package com.web.ecommerce.domain.coupon.controller;

import com.web.ecommerce.domain.coupon.dto.request.CreateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.request.UpdateCouponRequest;
import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.CouponSelectResponse;
import com.web.ecommerce.domain.coupon.dto.response.DownloadableCouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Coupon", description = "쿠폰 API")
public interface CouponController {

  @Operation(summary = "쿠폰 생성 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> createCoupon(CreateCouponRequest request);

  @Operation(summary = "쿠폰 목록 조회 (ADMIN)")
  ResponseEntity<BaseResponse<Page<CouponResponse>>> getCoupons(int page, int size);

  @Operation(summary = "쿠폰 단건 조회 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> getCoupon(Long couponId);

  @Operation(summary = "쿠폰 수정 (ADMIN)")
  ResponseEntity<BaseResponse<CouponResponse>> updateCoupon(Long couponId, UpdateCouponRequest request);

  @Operation(summary = "쿠폰 삭제 (ADMIN)")
  ResponseEntity<Void> deleteCoupon(Long couponId);

  @Operation(summary = "특정 회원에게 쿠폰 발급 (ADMIN)")
  ResponseEntity<BaseResponse<UserCouponResponse>> issueCoupon(Long couponId, Long userId);

  @Operation(summary = "회원 쿠폰 목록 조회")
  ResponseEntity<BaseResponse<Page<UserCouponResponse>>> getUserCoupons(Long userId, int page, int size);

  @Operation(summary = "쿠폰 사용 처리")
  ResponseEntity<BaseResponse<UserCouponResponse>> useCoupon(Long userId, Long userCouponId);

  @Operation(summary = "쿠폰 선택 목록 조회 (캠페인 생성용)", description = "cursor 기반 무한스크롤. cursor=0 또는 미입력 시 처음부터 조회.")
  ResponseEntity<BaseResponse<CursorResponse<CouponSelectResponse>>> getCouponSelectList(Long cursor, int size);

  @Operation(summary = "다운로드 가능 쿠폰 목록 조회", description = "마이페이지에 다운로드 가능한 쿠폰 목록. 이미 발급받은 쿠폰 및 수량 초과 쿠폰은 제외됩니다.")
  ResponseEntity<BaseResponse<List<DownloadableCouponResponse>>> getDownloadableCoupons();

  @Operation(summary = "쿠폰 다운로드 발급", description = "사용자가 팝업에서 쿠폰 다운로드 버튼 클릭 시 호출.")
  ResponseEntity<BaseResponse<UserCouponResponse>> downloadCoupon(Long couponId);

}
