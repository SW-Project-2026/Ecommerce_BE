package com.web.ecommerce.global.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum GlobalErrorCode implements BaseErrorCode {
  INVALID_INPUT_VALUE("GLOBAL001", "유효하지 않은 입력입니다.", HttpStatus.BAD_REQUEST),
  RESOURCE_NOT_FOUND("GLOBAL002", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
  INTERNAL_SERVER_ERROR("GLOBAL003", "서버 내부 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),
  PAGE_SORT_STYLE_WRONG("GLOBAL004", "Page Sort 설정 양식이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
  NAVER_API_CALL_FAILED("GLOBAL005", "네이버 쇼핑 API 호출에 실패했습니다.", HttpStatus.BAD_GATEWAY),
  NAVER_API_EMPTY_RESPONSE("GLOBAL006", "네이버 쇼핑 API 응답이 비어있습니다.", HttpStatus.NO_CONTENT);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
