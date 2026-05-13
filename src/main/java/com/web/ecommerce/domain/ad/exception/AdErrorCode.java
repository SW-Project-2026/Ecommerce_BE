package com.web.ecommerce.domain.ad.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AdErrorCode implements BaseErrorCode {

  AD_NOT_FOUND("AD001", "존재하지 않는 광고입니다.", HttpStatus.NOT_FOUND),
  AD_EXPOSURE_NOT_FOUND("AD002", "존재하지 않는 광고 노출 기록입니다.", HttpStatus.NOT_FOUND);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
