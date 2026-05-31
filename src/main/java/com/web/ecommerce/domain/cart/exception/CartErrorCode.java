package com.web.ecommerce.domain.cart.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CartErrorCode implements BaseErrorCode {

    CART_NOT_FOUND("CART001", "존재하지 않는 장바구니 항목입니다.", HttpStatus.NOT_FOUND),
    CART_ACCESS_DENIED("CART002", "해당 장바구니 항목에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_IN_CART("CART003", "이미 장바구니에 담긴 상품입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
