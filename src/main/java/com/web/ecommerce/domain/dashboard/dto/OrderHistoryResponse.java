package com.web.ecommerce.domain.dashboard.dto;

import java.util.List;

public record OrderHistoryResponse(
        Long orderId,
        String orderDate,
        String status,
        int finalAmount,
        List<OrderItem> items
) {
    public record OrderItem(String productName, int quantity, int unitPrice) {}
}
