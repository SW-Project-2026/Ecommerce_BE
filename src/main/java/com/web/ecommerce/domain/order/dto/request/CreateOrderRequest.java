package com.web.ecommerce.domain.order.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
@Schema(description = "주문 생성 요청")
public class CreateOrderRequest {

    @NotNull
    @Schema(description = "배송지 ID", example = "1")
    private Long addressId;

    @Schema(description = "사용할 유저 쿠폰 ID (없으면 생략)", example = "2")
    private Long userCouponId;

    @NotEmpty
    @Schema(description = "주문 상품 목록")
    private List<OrderItemRequest> items;

    @Getter
    @Schema(description = "주문 상품 항목")
    public static class OrderItemRequest {

        @NotNull
        @Schema(description = "상품 ID", example = "3")
        private Long productId;

        @Min(1)
        @Schema(description = "수량", example = "1")
        private int quantity;
    }
}
