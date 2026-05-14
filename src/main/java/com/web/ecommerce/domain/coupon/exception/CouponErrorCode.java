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
  ISSUE_LIMIT_EXCEEDED("COUPON006", "발급 수량이 초과되었습니다.", HttpStatus.BAD_REQUEST);

  private final String code;
  private final String message;
  private final HttpStatus status;
}
