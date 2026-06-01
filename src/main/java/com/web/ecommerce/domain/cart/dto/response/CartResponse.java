package com.web.ecommerce.domain.cart.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartResponse {

    private Long cartId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private int unitPrice;
    private int quantity;
    private int subtotal;
}
