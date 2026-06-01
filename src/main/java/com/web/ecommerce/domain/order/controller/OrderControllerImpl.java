package com.web.ecommerce.domain.order.controller;

import com.web.ecommerce.domain.order.dto.request.CreateOrderRequest;
import com.web.ecommerce.domain.order.dto.response.OrderResponse;
import com.web.ecommerce.domain.order.service.OrderService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.response.CursorResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderControllerImpl implements OrderController {

    private final OrderService orderService;

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<CursorResponse<OrderResponse>>> getOrders(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam(required = false) Long cursor,
            @RequestParam(defaultValue = "all") String period,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(BaseResponse.success(
                orderService.getOrders(userPrincipal.id(), cursor, period, size)));
    }

    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponse<OrderResponse>> getOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(BaseResponse.success(orderService.getOrder(userPrincipal.id(), orderId)));
    }

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateOrderRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "주문이 생성되었습니다.", orderService.createOrder(userPrincipal.id(), request)));
    }

    @Override
    @PatchMapping("/{orderId}/pay")
    public ResponseEntity<BaseResponse<OrderResponse>> payOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long orderId) {
        return ResponseEntity.ok(BaseResponse.success(orderService.payOrder(userPrincipal.id(), orderId)));
    }

    @Override
    @PatchMapping("/{orderId}/cancel")
    public ResponseEntity<Void> cancelOrder(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long orderId) {
        orderService.cancelOrder(userPrincipal.id(), orderId);
        return ResponseEntity.noContent().build();
    }
}
