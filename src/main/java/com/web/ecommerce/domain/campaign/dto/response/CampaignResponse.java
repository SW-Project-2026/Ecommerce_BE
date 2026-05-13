package com.web.ecommerce.domain.campaign.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Campaign 상세 응답 DTO")
public class CampaignResponse {

  private Long campaignId;
  private String campaignName;
  private String description;
  private String campaignGoalType;
  private String customerSegment;
  private String status;
  private String collectionType;
  private String batchCycle;
  private String batchTime;
  private String batchDayOfWeek;
  private Integer batchDayOfMonth;
  private String startedAt;
  private String endedAt;
  private String createdBy;
  private String createdAt;
  private String filterLogicalOperator;
  private List<CampaignFilterResponse> filters;
}
