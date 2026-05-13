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
@Schema(title = "쿠폰 응답 DTO")
public class CouponResponse {

  private Long couponId;
  private String name;
  private String code;
  private String discountType;
  private Integer discountAmount;
  private Integer minOrderAmount;
  private Integer maxDiscountAmount;
  private Integer expiredAt;
  private String issuanceMethod;
  private Integer issueLimit;
  private String createdAt;
}
