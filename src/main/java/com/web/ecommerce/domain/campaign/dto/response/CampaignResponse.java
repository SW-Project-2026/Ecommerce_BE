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
  private Integer couponRestrictionDays;
  private String issueType;
  private Long couponId;
  private String couponName;
  private Long adId;
  private String adName;

  @Schema(description = "메시지 타입 (SMS/LMS)")
  private String messageType;

  @Schema(description = "메시지 제목 (LMS 전용)")
  private String messageSubject;

  @Schema(description = "메시지 내용")
  private String messageContent;

  private List<CampaignFilterResponse> filters;
}
