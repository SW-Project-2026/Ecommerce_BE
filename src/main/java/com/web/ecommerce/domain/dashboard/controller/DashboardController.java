package com.web.ecommerce.domain.dashboard.controller;

import com.web.ecommerce.domain.dashboard.dto.AdminDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.CartItemResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.OrderHistoryResponse;
import com.web.ecommerce.domain.dashboard.dto.UserDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.UserSummaryResponse;
import com.web.ecommerce.domain.dashboard.service.DashboardService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "대시보드 API")
@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    // ──────────────── Admin ────────────────

    @Operation(summary = "전체 대시보드 조회", description = "관리자 전용 - 매출/DAU/인기상품/카테고리/이벤트/광고/쿠폰/가입·탈퇴 통계")
    @GetMapping("/admin")
    public ResponseEntity<BaseResponse<AdminDashboardResponse>> getAdminDashboard(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getAdminDashboard(days)));
    }

    @Operation(summary = "고객 개인 대시보드", description = "관리자 전용 - 특정 고객 상세 분석")
    @GetMapping("/customers/{userId}")
    public ResponseEntity<BaseResponse<CustomerDashboardResponse>> getCustomerDashboard(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomerDashboard(userId)));
    }

    @Operation(summary = "고객 장바구니", description = "관리자 전용 - 무한스크롤")
    @GetMapping("/customers/{userId}/cart")
    public ResponseEntity<BaseResponse<CursorResponse<CustomerDashboardResponse.CartItem>>> getCustomerCart(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomerCart(userId, cursor, size)));
    }

    @Operation(summary = "고객 구매이력", description = "관리자 전용 - 무한스크롤")
    @GetMapping("/customers/{userId}/orders")
    public ResponseEntity<BaseResponse<CursorResponse<CustomerDashboardResponse.OrderItem>>> getCustomerOrders(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomerOrders(userId, cursor, size)));
    }

    @Operation(summary = "사용자 목록 조회", description = "관리자 전용 - loginId/등급/최근접속/구매빈도/이탈위험/CTR/쿠폰사용률")
    @GetMapping("/admin/users")
    public ResponseEntity<BaseResponse<Page<UserSummaryResponse>>> getAdminUserList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getAdminUserList(page, size)));
    }

    // ──────────────── User ────────────────

    @Operation(summary = "개인 대시보드 조회", description = "로그인 유저 - 접속/구매/탈퇴위험/등급/CTR/쿠폰/키워드/시간대 통계")
    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserDashboardResponse>> getUserDashboard(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getUserDashboard(userPrincipal.id())));
    }

    @Operation(summary = "장바구니 목록", description = "개인 대시보드 - 무한스크롤")
    @GetMapping("/me/cart")
    public ResponseEntity<BaseResponse<CursorResponse<CartItemResponse>>> getUserCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(
                dashboardService.getUserCart(userPrincipal.id(), cursor, size)));
    }

    @Operation(summary = "구매이력 목록", description = "개인 대시보드 - 무한스크롤")
    @GetMapping("/me/orders")
    public ResponseEntity<BaseResponse<CursorResponse<OrderHistoryResponse>>> getUserOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(defaultValue = "0") Long cursor,
            @RequestParam(defaultValue = "20") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(
                dashboardService.getUserOrders(userPrincipal.id(), cursor, size)));
    }
}
