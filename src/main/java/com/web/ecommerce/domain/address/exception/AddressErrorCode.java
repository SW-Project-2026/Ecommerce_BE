package com.web.ecommerce.domain.address.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum AddressErrorCode implements BaseErrorCode {

    ADDRESS_NOT_FOUND("ADDRESS001", "존재하지 않는 배송지입니다.", HttpStatus.NOT_FOUND),
    ADDRESS_ACCESS_DENIED("ADDRESS002", "해당 배송지에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
