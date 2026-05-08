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
@Schema(title="Campaign 생성 응답 DTO")
public class CampaignResponse {

  private Long campaignId;
  private String campaignName;
  private String status;          // DRAFT(임시저장) / ACTIVE(저장)

  // 요약 (목록 카드에 바로 쓸 것들)
  private String campaignGoalType;
  private String customerSegment;
  private String startedAt;
  private String endedAt;
  private String collectionType;  // TRIGGERED / BATCH
  private Integer batchCycle;     // 배치일 때만
  // 메타
  private String createdAt;

  private List<CampaignFilterResponse> filters;

  //리워드
}
