package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.campaign.enums.LogicalOperator;
import com.web.ecommerce.domain.campaign.enums.Operator;
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
@Schema(title = "Campaign 생성 요청 DTO")
public class CreateCampaignRequest {

  @Schema(description = "캠페인 이름")
  private String campaignName;

  @Schema(description = "캠페인 설명")
  private String description;

  @Schema(description = "캠페인 목적(EARLY_RETENTION/CHURN_PREVENTION/REPURCHASE)", example = "EARLY_RETENTION")
  private String campaignGoalType;

  @Schema(description = "캠페인 타겟 고객군(VIP/NEW/GENERAL/DORMANT/ALL)", example = "NEW")
  private String customerSegment;

  @Schema(description = "시작일자")
  private String startedAt;

  @Schema(description = "종료 일자")
  private String endedAt;

  @Schema(description = "캠페인 데이터 수집 타입(TRIGGERED/BATCH)", example = "BATCH")
  private String collectionType;

  @Schema(description = "배치 주기(DAILY/WEEKLY/MONTHLY)")
  private BatchCycle batchCycle;

  @Schema(description = "배치 실행 시간(HH:mm)", example = "09:00")
  private String batchTime;

  @Schema(description = "배치 요일 - WEEKLY일 때만 사용(MONDAY~SUNDAY)", example = "MONDAY")
  private String batchDayOfWeek;

  @Schema(description = "배치 날짜 - MONTHLY일 때만 사용(1~31)", example = "15")
  private Integer batchDayOfMonth;

  @Schema(description = "필터 간 논리 연산자(AND/OR)", example = "AND")
  private LogicalOperator filterLogicalOperator;

  @Schema(description = "캠페인 필터 목록")
  private List<CreateCampaignFilter> filters;

  @Getter
  @AllArgsConstructor
  @NoArgsConstructor
  public static class CreateCampaignFilter {

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
