package com.web.ecommerce.domain.campaign.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "쿠폰 발송 결과 DTO")
public class CouponSendResponse {

  @Schema(description = "발송 대상 총 인원")
  private int totalTargetCount;

  @Schema(description = "발급 성공 인원")
  private int successCount;

  @Schema(description = "중복 발급 처리 인원 (이미 쿠폰 보유)")
  private int duplicateCount;

  @Schema(description = "발급 제한 기간 내 이력으로 제외된 인원")
  private int restrictedCount;

  @Schema(description = "쿠폰 수량 초과로 미발급된 인원")
  private int limitExceededCount;
}
