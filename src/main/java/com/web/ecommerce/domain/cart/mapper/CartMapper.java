package com.web.ecommerce.domain.cart.mapper;

import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.cart.entity.Cart;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

    public CartResponse toResponse(Cart cart) {
        int unitPrice = cart.getProduct().getMinPrice();
        return CartResponse.builder()
                .cartId(cart.getId())
                .productId(cart.getProduct().getProductId())
                .productName(cart.getProduct().getName())
                .imageUrl(cart.getProduct().getImageUrl())
                .unitPrice(unitPrice)
                .quantity(cart.getQuantity())
                .subtotal(unitPrice * cart.getQuantity())
                .build();
    }
}
