package com.web.ecommerce.domain.wishlist.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum WishlistErrorCode implements BaseErrorCode {

    WISHLIST_NOT_FOUND("WISH001", "존재하지 않는 찜 항목입니다.", HttpStatus.NOT_FOUND),
    WISHLIST_ACCESS_DENIED("WISH002", "해당 찜 항목에 접근 권한이 없습니다.", HttpStatus.FORBIDDEN),
    ALREADY_IN_WISHLIST("WISH003", "이미 찜한 상품입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
