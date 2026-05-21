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
@Schema(title = "쿠폰 선택 목록 응답 DTO")
public class CouponSelectResponse {

  private Long id;
  private String name;
  private String code;
  private String discountType;
  private Integer discountAmount;
  private Integer expiredAt;
}
