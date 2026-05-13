package com.web.ecommerce.domain.ad.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "광고 응답 DTO")
public class AdResponse {

  private Long adId;
  private String adName;
  private String targetType;
  private Long productId;
  private String category;
  private String keyword;
  private String createdAt;
}
