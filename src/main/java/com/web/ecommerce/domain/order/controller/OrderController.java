package com.web.ecommerce.domain.order.controller;

import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Order", description = "주문 API")
public interface OrderController {

    @Operation(summary = "내 주문 목록 조회", description = "period: all(전체), 1m(1개월), 3m(3개월), 6m(6개월) / cursor: 마지막 orderId (첫 요청 시 생략)")
    ResponseEntity<BaseResponse<CursorResponse<OrderResponse>>> getOrders(UserPrincipal userPrincipal, Long cursor, String period, int size);

    @Operation(summary = "주문 상세 조회")
    ResponseEntity<BaseResponse<OrderResponse>> getOrder(UserPrincipal userPrincipal, Long orderId);

    @Operation(summary = "주문 생성 및 결제", description = "주문 생성과 동시에 결제 완료 처리")
    ResponseEntity<BaseResponse<OrderResponse>> createOrder(UserPrincipal userPrincipal, CreateOrderRequest request);

    @Operation(summary = "주문 취소", description = "PENDING, SHIPPING 상태의 주문만 취소 가능")
    ResponseEntity<Void> cancelOrder(UserPrincipal userPrincipal, Long orderId);
}
