package com.web.ecommerce.domain.cart.service;

import com.web.ecommerce.domain.cart.dto.request.AddCartRequest;
import com.web.ecommerce.domain.cart.dto.request.UpdateCartRequest;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;

import java.util.List;

public interface CartService {

    List<CartResponse> getCart(Long userId);

    CartResponse addToCart(Long userId, AddCartRequest request);

    CartResponse updateCart(Long userId, Long cartId, UpdateCartRequest request);

    void removeFromCart(Long userId, Long cartId);

    void clearCart(Long userId);
}
