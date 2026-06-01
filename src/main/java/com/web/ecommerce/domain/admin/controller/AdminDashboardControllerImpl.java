package com.web.ecommerce.domain.admin.controller;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.admin.service.AdminDashboardService;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<CursorResponse<OrderResponse>>> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "all") String period,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                adminDashboardService.getUserOrders(userId, cursor, period, size)));
    }

    @Override
    @GetMapping("/users/{userId}/cart")
    public ResponseEntity<BaseResponse<CursorResponse<CartResponse>>> getUserCart(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                adminDashboardService.getUserCart(userId, cursor, size)));
    }

    @Override
    @GetMapping("/users/{userId}/wishlist")
    public ResponseEntity<BaseResponse<CursorResponse<WishlistResponse>>> getUserWishlist(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "6") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                adminDashboardService.getUserWishlist(userId, cursor, size)));
    }
}
