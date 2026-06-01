package com.web.ecommerce.domain.order.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OrderErrorCode implements BaseErrorCode {

    ORDER_NOT_FOUND("ORDER001", "존재하지 않는 주문입니다.", HttpStatus.NOT_FOUND),
    ORDER_ACCESS_DENIED("ORDER002", "해당 주문에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ORDER_CANNOT_CANCEL("ORDER003", "취소할 수 없는 주문 상태입니다.", HttpStatus.BAD_REQUEST),
    ORDER_CANNOT_PAY("ORDER006", "결제할 수 없는 주문 상태입니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK("ORDER004", "재고가 부족합니다.", HttpStatus.BAD_REQUEST),
    COUPON_MIN_ORDER_NOT_MET("ORDER005", "쿠폰 최소 주문금액을 충족하지 않습니다.", HttpStatus.BAD_REQUEST);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
