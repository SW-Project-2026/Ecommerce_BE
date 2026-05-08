package com.web.ecommerce.domain.campaign.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CampaignErrorCode implements BaseErrorCode {

  CAMPAIGN_NOT_FOUND("CAMPAIGN001", "존재하지 않는 캠페인입니다.", HttpStatus.NOT_FOUND),
  INVALID_STATUS_TRANSITION("CAMPAIGN002", "유효하지 않은 상태 전환입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
