package com.web.ecommerce.domain.cart.service;

import com.web.ecommerce.domain.cart.dto.request.AddCartRequest;
import com.web.ecommerce.domain.cart.dto.request.UpdateCartRequest;
import com.web.ecommerce.domain.cart.dto.response.CartResponse;
import com.web.ecommerce.domain.cart.entity.Cart;
import com.web.ecommerce.domain.cart.exception.CartErrorCode;
import com.web.ecommerce.domain.cart.mapper.CartMapper;
import com.web.ecommerce.domain.cart.repository.CartRepository;
import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.exception.ProductErrorCode;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CartResponse> getCart(Long userId) {
        return cartRepository.findAllByUserId(userId).stream()
                .map(cartMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CartResponse addToCart(Long userId, AddCartRequest request) {
        if (cartRepository.findByUserIdAndProductProductId(userId, request.getProductId()).isPresent()) {
            throw new CustomException(CartErrorCode.ALREADY_IN_CART);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Cart cart = Cart.builder()
                .user(user)
                .product(product)
                .quantity(request.getQuantity())
                .build();

        return cartMapper.toResponse(cartRepository.save(cart));
    }

    @Override
    @Transactional
    public CartResponse updateCart(Long userId, Long cartId, UpdateCartRequest request) {
        Cart cart = getCartOwnedByUser(userId, cartId);
        cart.updateQuantity(request.getQuantity());
        return cartMapper.toResponse(cart);
    }

    @Override
    @Transactional
    public void removeFromCart(Long userId, Long cartId) {
        Cart cart = getCartOwnedByUser(userId, cartId);
        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        cartRepository.deleteAllByUserId(userId);
    }

    private Cart getCartOwnedByUser(Long userId, Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CustomException(CartErrorCode.CART_NOT_FOUND));
        if (!cart.getUser().getId().equals(userId)) {
            throw new CustomException(CartErrorCode.CART_ACCESS_DENIED);
        }
        return cart;
    }
}
