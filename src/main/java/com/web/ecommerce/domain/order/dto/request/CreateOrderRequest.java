package com.web.ecommerce.domain.order.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class CreateOrderRequest {

    @NotNull
    private Long addressId;

    private Long userCouponId;

    @NotEmpty
    private List<OrderItemRequest> items;

    @Getter
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @Min(1)
        private int quantity;
    }
}
