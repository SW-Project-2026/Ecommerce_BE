package com.web.ecommerce.domain.product.exception;

import com.web.ecommerce.global.exception.model.BaseErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements BaseErrorCode {

    PRODUCT_NOT_FOUND("PRODUCT001", "상품을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_ALREADY_EXISTS("PRODUCT002", "이미 존재하는 상품입니다.", HttpStatus.CONFLICT),
    PRODUCT_INVALID_PRICE("PRODUCT003", "상품 가격은 0원 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_STOCK("PRODUCT004", "재고 수량은 0개 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    PRODUCT_IMAGE_NOT_FOUND("PRODUCT005", "상품 이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    PRODUCT_INACTIVE("PRODUCT006", "비활성화된 상품입니다.", HttpStatus.BAD_REQUEST),
    NAVER_API_CALL_FAILED("PRODUCT007", "네이버 쇼핑 API 호출에 실패했습니다.", HttpStatus.BAD_GATEWAY),
    NAVER_API_EMPTY_RESPONSE("PRODUCT008", "네이버 쇼핑 API 응답이 비어있습니다.", HttpStatus.NO_CONTENT),
    SCHEDULE_NOT_FOUND("PRODUCT009", "등록된 자동 수집 스케줄이 없습니다.", HttpStatus.NOT_FOUND),
    SCHEDULE_ALREADY_EXISTS("PRODUCT010", "이미 활성화된 스케줄이 있습니다. 먼저 기존 스케줄을 취소해주세요.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus status;
}
