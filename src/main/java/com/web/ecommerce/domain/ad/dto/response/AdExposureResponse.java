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
@Schema(title = "광고 노출 응답 DTO")
public class AdExposureResponse {

  private Long adExposureId;
  private Long adId;
  private String adName;
  private Long userId;
  private Boolean clicked;
  private String exposedAt;
  private String clickedAt;
}
