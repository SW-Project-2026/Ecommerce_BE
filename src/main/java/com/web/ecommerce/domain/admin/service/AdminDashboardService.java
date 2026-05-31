package com.web.ecommerce.domain.admin.service;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminDashboardService {

    AdminDashboardResponse getDashboard();

    Page<OrderResponse> getUserOrders(Long userId, Pageable pageable);

    List<CartResponse> getUserCart(Long userId);
}
