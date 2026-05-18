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
@Schema(title = "Campaign 목록 응답 DTO")
public class CampaignSummaryResponse {

  private Long campaignId;
  private String campaignGoalType;
  private String customerSegment;
  private String campaignName;
  private String status;
  private String startedAt;
  private String endedAt;
  private String createdBy;
  private String createdAt;
}
