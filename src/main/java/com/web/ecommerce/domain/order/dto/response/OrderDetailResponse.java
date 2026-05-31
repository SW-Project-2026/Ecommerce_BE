package com.web.ecommerce.domain.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDetailResponse {

    private Long orderItemId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private int quantity;
    private int unitPrice;
    private int subtotal;
}
