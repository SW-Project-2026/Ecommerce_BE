package com.web.ecommerce.domain.campaign.dto.reqeust;

import com.web.ecommerce.domain.campaign.enums.BatchCycle;
import com.web.ecommerce.domain.campaign.enums.DuplicatePolicy;
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

  @Schema(description = "쿠폰 중복 발급 제한 기간 (N일 이내 동일 쿠폰 발급 이력 있는 사용자 제외, null이면 제한 없음)")
  private Integer couponRestrictionDays;

  @Schema(description = "발급 방식 (AUTO/DOWNLOAD/MESSAGE)", example = "MESSAGE")
  private String issueType;

  @Schema(description = "쿠폰 ID (광고와 동시 설정 불가, null이면 해제)")
  private Long couponId;

  @Schema(description = "광고 ID (쿠폰과 동시 설정 불가, null이면 해제)")
  private Long adId;

  @Schema(description = "메시지 타입 (SMS/LMS) - BATCH 캠페인 자동 발송 시 사용", example = "LMS")
  private String messageType;

  @Schema(description = "메시지 제목 (LMS 전용) - BATCH 캠페인 자동 발송 시 사용", example = "특별 쿠폰 안내")
  private String messageSubject;

  @Schema(description = "메시지 내용 - BATCH 캠페인 자동 발송 시 사용", example = "안녕하세요. 특별 쿠폰을 발급해 드립니다.")
  private String messageContent;

  @Schema(description = "중복 발송 정책 (CHECK: N일 내 발송 이력 있으면 스킵 / IGNORE: 이력 무시하고 발송)", example = "CHECK")
  private DuplicatePolicy duplicatePolicy;

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
