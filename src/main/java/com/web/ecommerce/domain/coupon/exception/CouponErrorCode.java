package com.web.ecommerce.domain.coupon.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CouponErrorCode implements BaseErrorCode {

  COUPON_NOT_FOUND("COUPON001", "존재하지 않는 쿠폰입니다.", HttpStatus.NOT_FOUND),
  DUPLICATE_COUPON_CODE("COUPON002", "이미 사용 중인 쿠폰 코드입니다.", HttpStatus.CONFLICT),
  ALREADY_ISSUED("COUPON003", "이미 발급된 쿠폰입니다.", HttpStatus.CONFLICT),
  USER_COUPON_NOT_FOUND("COUPON004", "존재하지 않는 회원 쿠폰입니다.", HttpStatus.NOT_FOUND),
  ALREADY_USED("COUPON005", "이미 사용된 쿠폰입니다.", HttpStatus.BAD_REQUEST),
  ISSUE_LIMIT_EXCEEDED("COUPON006", "발급 수량이 초과되었습니다.", HttpStatus.BAD_REQUEST),
  INVALID_COUPON_CODE_FORMAT("COUPON007", "쿠폰 코드는 영문 대문자와 숫자만 허용됩니다.", HttpStatus.BAD_REQUEST),
  INVALID_DISCOUNT_AMOUNT("COUPON008", "퍼센트 할인은 1~100 사이의 값이어야 합니다.", HttpStatus.BAD_REQUEST),
  MIN_ORDER_AMOUNT_REQUIRED("COUPON009", "정액 할인은 최소 주문 금액이 필수입니다.", HttpStatus.BAD_REQUEST),
  COUPON_EXPIRED("COUPON010", "만료된 쿠폰입니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
