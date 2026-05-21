package com.web.ecommerce.domain.coupon.mapper;

import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
import com.web.ecommerce.domain.coupon.enums.CouponStatus;
import org.springframework.stereotype.Component;

@Component
public class CouponMapper {

  public CouponResponse toCouponResponse(Coupon coupon) {
    return CouponResponse.builder()
        .couponId(coupon.getId())
        .name(coupon.getName())
        .code(coupon.getCode())
        .discountType(coupon.getDiscountType().name())
        .discountAmount(coupon.getDiscountAmount())
        .minOrderAmount(coupon.getMinOrderAmount())
        .maxDiscountAmount(coupon.getMaxDiscountAmount())
        .expiredAt(coupon.getExpiredAt())
        .issueLimit(coupon.getIssueLimit())
        .createdAt(coupon.getCreatedAt() != null ? coupon.getCreatedAt().toString() : null)
        .build();
  }

  public UserCouponResponse toUserCouponResponse(UserCoupon userCoupon) {
    Coupon coupon = userCoupon.getCoupon();

    String expiredAt = null;
    if (coupon.getExpiredAt() != null && userCoupon.getCreatedAt() != null) {
      expiredAt = userCoupon.getCreatedAt().toLocalDate()
          .plusDays(coupon.getExpiredAt()).toString();
    }

    String statusName = userCoupon.getStatus().name();

    // 계산된 만료일이 현재 날짜보다 과거라면 화면에는 EXPIRED로 내려줌
    if (userCoupon.getStatus() == CouponStatus.AVAILABLE && expiredAt != null) {
      if (java.time.LocalDate.parse(expiredAt).isBefore(java.time.LocalDate.now())) {
        statusName = CouponStatus.EXPIRED.name();
      }
    }

    return UserCouponResponse.builder()
        .userCouponId(userCoupon.getId())
        .couponId(coupon.getId())
        .couponName(coupon.getName())
        .code(coupon.getCode())
        .discountType(coupon.getDiscountType().name())
        .discountAmount(coupon.getDiscountAmount())
        .minOrderAmount(coupon.getMinOrderAmount())
        .maxDiscountAmount(coupon.getMaxDiscountAmount())
        .expiredAt(expiredAt)
        .status(statusName)
        .usedAt(userCoupon.getUsedAt() != null ? userCoupon.getUsedAt().toString() : null)
        .issuedAt(userCoupon.getCreatedAt() != null ? userCoupon.getCreatedAt().toString() : null)
        .build();
  }
}
