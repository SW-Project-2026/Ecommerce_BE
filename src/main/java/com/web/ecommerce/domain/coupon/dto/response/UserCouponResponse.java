package com.web.ecommerce.domain.coupon.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "회원 쿠폰 응답 DTO")
public class UserCouponResponse {

  private Long userCouponId;
  private Long couponId;
  private String couponName;
  private String code;
  private String discountType;
  private Integer discountAmount;
  private Integer minOrderAmount;
  private Integer maxDiscountAmount;
  private String expiredAt;
  private Boolean isUsed;
  private String usedAt;
  private String issuedAt;
}
