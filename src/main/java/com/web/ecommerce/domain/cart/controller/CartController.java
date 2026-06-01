package com.web.ecommerce.domain.cart.controller;

import com.web.ecommerce.domain.cart.dto.request.AddCartRequest;
import com.web.ecommerce.domain.cart.dto.request.UpdateCartRequest;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Cart", description = "장바구니 API")
public interface CartController {

    @Operation(summary = "내 장바구니 조회")
    ResponseEntity<BaseResponse<List<CartResponse>>> getCart(UserPrincipal userPrincipal);

    @Operation(summary = "장바구니 상품 추가")
    ResponseEntity<BaseResponse<CartResponse>> addToCart(UserPrincipal userPrincipal, AddCartRequest request);

    @Operation(summary = "장바구니 수량 변경")
    ResponseEntity<BaseResponse<CartResponse>> updateCart(UserPrincipal userPrincipal, Long cartId, UpdateCartRequest request);

    @Operation(summary = "장바구니 상품 삭제")
    ResponseEntity<Void> removeFromCart(UserPrincipal userPrincipal, Long cartId);

    @Operation(summary = "장바구니 전체 비우기")
    ResponseEntity<Void> clearCart(UserPrincipal userPrincipal);
}
