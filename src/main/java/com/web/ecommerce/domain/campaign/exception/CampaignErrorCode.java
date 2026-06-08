package com.web.ecommerce.domain.campaign.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CampaignErrorCode implements BaseErrorCode {

  CAMPAIGN_NOT_FOUND("CAMPAIGN001", "존재하지 않는 캠페인입니다.", HttpStatus.NOT_FOUND),
  INVALID_STATUS_TRANSITION("CAMPAIGN002", "유효하지 않은 상태 전환입니다.", HttpStatus.BAD_REQUEST),
  INVALID_OPERATOR_FOR_FIELD_TYPE("CAMPAIGN003", "해당 필드 타입에서 사용할 수 없는 연산자입니다.", HttpStatus.BAD_REQUEST),
  CANNOT_SET_BOTH_AD_AND_COUPON("CAMPAIGN004", "광고와 쿠폰은 동시에 설정할 수 없습니다.", HttpStatus.BAD_REQUEST),
  CAMPAIGN_HAS_NO_COUPON("CAMPAIGN005", "쿠폰이 연결되지 않은 캠페인입니다.", HttpStatus.BAD_REQUEST),
  NOT_TRIGGERED_CAMPAIGN("CAMPAIGN006", "이벤트 트리거 캠페인이 아닙니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
