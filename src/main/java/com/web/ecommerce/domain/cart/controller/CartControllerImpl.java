package com.web.ecommerce.domain.cart.controller;

import com.web.ecommerce.domain.cart.dto.request.AddCartRequest;
import com.web.ecommerce.domain.cart.dto.request.UpdateCartRequest;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.cart.service.CartService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartControllerImpl implements CartController {

    private final CartService cartService;

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<List<CartResponse>>> getCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(BaseResponse.success(cartService.getCart(userPrincipal.id())));
    }

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<CartResponse>> addToCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody AddCartRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "장바구니에 추가되었습니다.", cartService.addToCart(userPrincipal.id(), request)));
    }

    @Override
    @PatchMapping("/{cartId}")
    public ResponseEntity<BaseResponse<CartResponse>> updateCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long cartId,
            @Valid @RequestBody UpdateCartRequest request) {
        return ResponseEntity.ok(BaseResponse.success(cartService.updateCart(userPrincipal.id(), cartId, request)));
    }

    @Override
    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> removeFromCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long cartId) {
        cartService.removeFromCart(userPrincipal.id(), cartId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @DeleteMapping
    public ResponseEntity<Void> clearCart(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        cartService.clearCart(userPrincipal.id());
        return ResponseEntity.noContent().build();
    }
}
