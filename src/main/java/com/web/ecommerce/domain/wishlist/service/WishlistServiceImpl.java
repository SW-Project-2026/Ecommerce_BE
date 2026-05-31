package com.web.ecommerce.domain.wishlist.service;

import com.web.ecommerce.domain.product.entity.Product;
import com.web.ecommerce.domain.product.exception.ProductErrorCode;
import com.web.ecommerce.domain.product.repository.ProductRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.domain.wishlist.entity.Wishlist;
import com.web.ecommerce.domain.wishlist.exception.WishlistErrorCode;
import com.web.ecommerce.domain.wishlist.mapper.WishlistMapper;
import com.web.ecommerce.domain.wishlist.repository.WishlistRepository;
import com.web.ecommerce.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishlistMapper wishlistMapper;

    @Override
    @Transactional(readOnly = true)
    public List<WishlistResponse> getWishlist(Long userId) {
        return wishlistRepository.findAllByUserId(userId).stream()
                .map(wishlistMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public WishlistResponse addToWishlist(Long userId, Long productId) {
        if (wishlistRepository.existsByUserIdAndProductProductId(userId, productId)) {
            throw new CustomException(WishlistErrorCode.ALREADY_IN_WISHLIST);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ProductErrorCode.PRODUCT_NOT_FOUND));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .product(product)
                .build();

        return wishlistMapper.toResponse(wishlistRepository.save(wishlist));
    }

    @Override
    @Transactional
    public void removeFromWishlist(Long userId, Long wishId) {
        Wishlist wishlist = wishlistRepository.findById(wishId)
                .orElseThrow(() -> new CustomException(WishlistErrorCode.WISHLIST_NOT_FOUND));
        if (!wishlist.getUser().getId().equals(userId)) {
            throw new CustomException(WishlistErrorCode.WISHLIST_ACCESS_DENIED);
        }
        wishlistRepository.delete(wishlist);
    }
}
