package com.web.ecommerce.domain.order.service;

import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.global.response.CursorResponse;

public interface OrderService {

    CursorResponse<OrderResponse> getOrders(Long userId, Long cursor, String period, int size);

    OrderResponse getOrder(Long userId, Long orderId);

    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    void cancelOrder(Long userId, Long orderId);
}
