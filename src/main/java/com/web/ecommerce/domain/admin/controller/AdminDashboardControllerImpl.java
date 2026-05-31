package com.web.ecommerce.domain.admin.controller;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.admin.service.AdminDashboardService;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.global.response.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminDashboardControllerImpl implements AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @Override
    @GetMapping("/dashboard")
    public ResponseEntity<BaseResponse<AdminDashboardResponse>> getDashboard() {
        return ResponseEntity.ok(BaseResponse.success(adminDashboardService.getDashboard()));
    }

    @Override
    @GetMapping("/users/{userId}/orders")
    public ResponseEntity<BaseResponse<Page<OrderResponse>>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                adminDashboardService.getUserOrders(userId, PageRequest.of(page, size))));
    }

    @Override
    @GetMapping("/users/{userId}/cart")
    public ResponseEntity<BaseResponse<List<CartResponse>>> getUserCart(
            @PathVariable Long userId) {
        return ResponseEntity.ok(BaseResponse.success(adminDashboardService.getUserCart(userId)));
    }
}
