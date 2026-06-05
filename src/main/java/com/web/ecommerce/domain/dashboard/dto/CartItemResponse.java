package com.web.ecommerce.domain.dashboard.dto;

public record CartItemResponse(
        Long cartId,
        Long productId,
        String productName,
        String productImage,
        int unitPrice,
        int quantity,
        int totalPrice
) {}
