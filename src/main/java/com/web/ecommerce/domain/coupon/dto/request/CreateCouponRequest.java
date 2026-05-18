package com.web.ecommerce.domain.coupon.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "쿠폰 생성 요청 DTO")
public class CreateCouponRequest {

  @Schema(description = "쿠폰 이름")
  private String name;

  @Schema(description = "쿠폰 코드")
  private String code;

  @Schema(description = "할인 유형 (FIXED/RATE)")
  private String discountType;

  @Schema(description = "할인 값 or %")
  private Integer discountAmount;

  @Schema(description = "최소 주문 금액")
  private Integer minOrderAmount;

  @Schema(description = "최대 할인 금액 (정률 할인 시 상한선)")
  private Integer maxDiscountAmount;

  @Schema(description = "유효 기간 (발급일 기준 N일)", example = "30")
  private Integer expiredAt;

  @Schema(description = "발급 방식 (AUTO/MANUAL/DOWNLOAD)")
  private String issuanceMethod;

  @Schema(description = "발급 수량 제한 (null이면 무제한)")
  private Integer issueLimit;
}
