package com.web.ecommerce.domain.order.service;

import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    Page<OrderResponse> getOrders(Long userId, Pageable pageable);

    OrderResponse getOrder(Long userId, Long orderId);

    OrderResponse createOrder(Long userId, CreateOrderRequest request);

    void cancelOrder(Long userId, Long orderId);
}
