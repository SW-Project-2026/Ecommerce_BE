package com.web.ecommerce.domain.admin.controller;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Admin Dashboard", description = "관리자 대시보드 API")
public interface AdminDashboardController {

    @Operation(summary = "전체 대시보드 통계")
    ResponseEntity<BaseResponse<AdminDashboardResponse>> getDashboard();

    @Operation(summary = "특정 유저 주문 목록")
    ResponseEntity<BaseResponse<Page<OrderResponse>>> getUserOrders(Long userId, int page, int size);

    @Operation(summary = "특정 유저 장바구니")
    ResponseEntity<BaseResponse<List<CartResponse>>> getUserCart(Long userId);
}
