package com.web.ecommerce.domain.ad.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Schema(title = "광고 생성 요청 DTO")
public class CreateAdRequest {

  @Schema(description = "광고 이름")
  private String adName;

  @Schema(description = "타겟 유형 (PRODUCT/CATEGORY/KEYWORD)")
  private String targetType;

  @Schema(description = "상품 ID (targetType=PRODUCT일 때)")
  private Long productId;

  @Schema(description = "카테고리 (targetType=CATEGORY일 때, BEAUTY/FASHION_ACCESSORY/LIVING_HEALTH/FURNITURE_INTERIOR/FOOD/SPORTS_LEISURE/DIGITAL_APPLIANCE/FASHION_CLOTHING)")
  private String category;

  @Schema(description = "키워드 (targetType=KEYWORD일 때)")
  private String keyword;
}
