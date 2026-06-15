package com.web.ecommerce.domain.order.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Builder
@Schema(description = "주문 응답")
public class OrderResponse {

    @Schema(description = "주문 ID", example = "1")
    private Long orderId;

    @Schema(description = "주문 일시", example = "2026-06-01T14:30:00")
    private String orderDate;

    @Schema(description = "주문 상태 (PENDING/SHIPPING/DELIVERED/CANCELLED)", example = "PENDING")
    private String status;

    @Schema(description = "주문 상태 한국어", example = "주문 완료")
    private String statusLabel;

    @Schema(description = "주문 총액 (할인 전)", example = "50000")
    private int totalAmount;

    @Schema(description = "할인 금액", example = "5000")
    private int discountAmount;

    @Schema(description = "최종 결제 금액", example = "45000")
    private int finalAmount;

    @Schema(description = "도로명 주소", example = "서울시 강남구 테헤란로 427")
    private String roadNameAddress;

    @Schema(description = "상세 주소", example = "101호")
    private String addressDetail;

    @Schema(description = "주문 상품 목록")
    private List<OrderDetailResponse> items;

    @Setter
    @Schema(description = "주문 완료 시 자동 발급된 쿠폰명 목록 (없으면 null)", example = "[\"신규 주문 감사 쿠폰\"]")
    private List<String> issuedCouponNames;
}
