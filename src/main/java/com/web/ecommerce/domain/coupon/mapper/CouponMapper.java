package com.web.ecommerce.domain.coupon.mapper;

import com.web.ecommerce.domain.coupon.dto.response.CouponResponse;
import com.web.ecommerce.domain.coupon.dto.response.UserCouponResponse;
import com.web.ecommerce.domain.coupon.entity.Coupon;
import com.web.ecommerce.domain.coupon.entity.UserCoupon;
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
        .issuanceMethod(coupon.getIssuanceMethod().name())
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
        .isUsed(userCoupon.getStatus())
        .usedAt(userCoupon.getUsedAt() != null ? userCoupon.getUsedAt().toString() : null)
        .issuedAt(userCoupon.getCreatedAt() != null ? userCoupon.getCreatedAt().toString() : null)
        .build();
  }
}
