package com.web.ecommerce.domain.campaign.dto.reqeust;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Campaign 상태 변경 요청 DTO")
public class UpdateCampaignStatusRequest {

  @Schema(description = "캠페인 상태(PENDING/IN_PROGRESS/PAUSED/ENDED)")
  private String status;
}
