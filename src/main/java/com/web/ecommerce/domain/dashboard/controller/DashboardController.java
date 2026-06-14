package com.web.ecommerce.domain.dashboard.controller;

import com.web.ecommerce.domain.dashboard.dto.CustomerDashboardResponse;
import com.web.ecommerce.domain.dashboard.dto.CustomerListResponse;
import com.web.ecommerce.domain.dashboard.dto.DashboardSummaryResponse;
import com.web.ecommerce.domain.dashboard.dto.MonthlyStatsResponse;
import com.web.ecommerce.domain.dashboard.service.DashboardService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
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

    @Operation(summary = "전체 요약 지표", description = "총 고객수 / CTR / 쿠폰 사용률 (기본값: 최근 30일)")
    @GetMapping("/summary")
    public ResponseEntity<BaseResponse<DashboardSummaryResponse>> getSummary(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getSummary(from, to)));
    }

    @Operation(summary = "월별 신규 가입·탈퇴율", description = "최근 12개월 기준")
    @GetMapping("/monthly-stats")
    public ResponseEntity<BaseResponse<MonthlyStatsResponse>> getMonthlyStats() {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getMonthlyStats()));
    }

    @Operation(summary = "고객 목록", description = "페이지네이션 + 검색 + 필터 (기본값: 최근 30일)")
    @GetMapping("/customers")
    public ResponseEntity<BaseResponse<CustomerListResponse>> getCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomers(page, size, search, filter, from, to)));
    }

    @Operation(summary = "고객 개인 대시보드", description = "관리자 전용 - 특정 고객 상세 분석 (기본값: 최근 30일)")
    @GetMapping("/customers/{userId}")
    public ResponseEntity<BaseResponse<CustomerDashboardResponse>> getCustomerDashboard(
            @PathVariable Long userId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomerDashboard(userId, from, to)));
    }

    @Operation(summary = "고객 찜 목록", description = "관리자 전용 - 무한스크롤")
    @GetMapping("/customers/{userId}/wishlist")
    public ResponseEntity<BaseResponse<CursorResponse<CustomerDashboardResponse.WishlistItem>>> getCustomerWishlist(
            @PathVariable Long userId,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "4") int size
    ) {
        return ResponseEntity.ok(BaseResponse.success(dashboardService.getCustomerWishlist(userId, cursor, size)));
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
}
