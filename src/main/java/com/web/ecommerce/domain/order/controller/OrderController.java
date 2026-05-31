package com.web.ecommerce.domain.order.controller;

import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@Tag(name = "Order", description = "주문 API")
public interface OrderController {

    @Operation(summary = "내 주문 목록 조회")
    ResponseEntity<BaseResponse<Page<OrderResponse>>> getOrders(UserPrincipal userPrincipal, int page, int size);

    @Operation(summary = "주문 상세 조회")
    ResponseEntity<BaseResponse<OrderResponse>> getOrder(UserPrincipal userPrincipal, Long orderId);

    @Operation(summary = "주문 생성")
    ResponseEntity<BaseResponse<OrderResponse>> createOrder(UserPrincipal userPrincipal, CreateOrderRequest request);

    @Operation(summary = "주문 취소", description = "PENDING 상태의 주문만 취소 가능")
    ResponseEntity<Void> cancelOrder(UserPrincipal userPrincipal, Long orderId);
}
