package com.web.ecommerce.domain.wishlist.service;

import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;

import java.util.List;

public interface WishlistService {

    List<WishlistResponse> getWishlist(Long userId);

    WishlistResponse addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long userId, Long wishId);
}
