package com.web.ecommerce.domain.admin.controller;

import com.web.ecommerce.domain.admin.dto.response.AdminDashboardResponse;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Admin Dashboard", description = "관리자 대시보드 API")
public interface AdminDashboardController {

    @Operation(summary = "전체 대시보드 통계")
    ResponseEntity<BaseResponse<AdminDashboardResponse>> getDashboard();

    @Operation(summary = "특정 유저 주문 목록 (커서 기반)", description = "period: all, 1m, 3m, 6m / cursor: 마지막 orderId (첫 요청 시 생략)")
    ResponseEntity<BaseResponse<CursorResponse<OrderResponse>>> getUserOrders(Long userId, Long cursor, String period, int size);

    @Operation(summary = "특정 유저 장바구니 (커서 기반)", description = "cursor: 마지막 cartId (첫 요청 시 생략)")
    ResponseEntity<BaseResponse<CursorResponse<CartResponse>>> getUserCart(Long userId, Long cursor, int size);

    @Operation(summary = "특정 유저 찜 목록 (커서 기반)", description = "cursor: 마지막 wishId (첫 요청 시 생략)")
    ResponseEntity<BaseResponse<CursorResponse<WishlistResponse>>> getUserWishlist(Long userId, Long cursor, int size);
}
