package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.LogicalOperator;
import com.web.ecommerce.domain.campaign.enums.Operator;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "Campaign 수정 요청 DTO")
public class UpdateCampaignRequest {

  @Schema(description = "캠페인 이름")
  private String campaignName;

  @Schema(description = "캠페인 설명")
  private String description;

  @Schema(description = "캠페인 목적(EARLY_RETENTION/CHURN_PREVENTION/REPURCHASE)")
  private String campaignGoalType;

  @Schema(description = "캠페인 타겟 고객군(VIP/NEW/GENERAL/DORMANT/ALL)")
  private String customerSegment;

  @Schema(description = "시작일자")
  private String startedAt;

  @Schema(description = "종료 일자")
  private String endedAt;

  @Schema(description = "캠페인 데이터 수집 타입(TRIGGERED/BATCH)")
  private String collectionType;

  @Schema(description = "배치 주기")
  private Integer batchCycle;

  @Schema(description = "보상 중복 수령 여부")
  private Boolean isDuplicate;

  @Schema(description = "필터 간 논리 연산자(AND/OR)", example = "AND")
  private LogicalOperator filterLogicalOperator;

  @Schema(description = "캠페인 필터 목록")
  private List<UpdateCampaignFilter> filters;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class UpdateCampaignFilter {

    @Schema(description = "이벤트 ID")
    private Long eventId;

    @Schema(description = "이벤트 필드 이름")
    private String eventFieldName;

    @Schema(description = "필드 연산자 (EQUALS/NOT_EQUALS/CONTAINS/NOT_CONTAINS/GT/GTE/LT/LTE/BETWEEN)")
    private Operator operator;

    @Schema(description = "필드 값")
    private String value;

    @Schema(description = "기간")
    private Integer periodDays;
  }
}
