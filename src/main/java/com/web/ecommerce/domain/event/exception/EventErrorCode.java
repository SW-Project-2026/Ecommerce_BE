package com.web.ecommerce.domain.event.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum EventErrorCode implements BaseErrorCode {

  EVENT_NOT_FOUND("EVENT001", "존재하지 않는 이벤트입니다.", HttpStatus.NOT_FOUND),
  EVENT_FIELD_NOT_FOUND("EVENT002", "존재하지 않는 이벤트 필드입니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_EVENT_NAME("EVENT003", "이미 존재하는 이벤트명입니다.", HttpStatus.CONFLICT),
  DUPLICATE_FIELD_NAME("EVENT004", "이미 존재하는 필드명입니다.", HttpStatus.CONFLICT);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
