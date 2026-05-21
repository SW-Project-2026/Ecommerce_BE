package com.web.ecommerce.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "쿠폰 수정 요청 DTO")
public class UpdateCouponRequest {

  @Schema(description = "쿠폰 이름")
  private String name;

  @Schema(description = "쿠폰 코드 (영문 대문자/숫자만 허용)", example = "WELCOME25")
  private String code;

  @Schema(description = "할인 유형 (PERCENT/FIXED)", example = "PERCENT")
  private String discountType;

  @Schema(description = "할인 값 (PERCENT: 1~100, FIXED: 금액)")
  private Integer discountAmount;

  @Schema(description = "최소 주문 금액 (FIXED 타입 시 필수)")
  private Integer minOrderAmount;

  @Schema(description = "최대 할인 금액 (nullable)")
  private Integer maxDiscountAmount;

  @Schema(description = "유효 기간 (발급일 기준 N일)", example = "30")
  private Integer expiredAt;

  @Schema(description = "발급 수량 제한 (null이면 무제한)")
  private Integer issueLimit;
}
