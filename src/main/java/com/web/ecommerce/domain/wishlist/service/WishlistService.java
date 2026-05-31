package com.web.ecommerce.domain.wishlist.service;

import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.global.response.CursorResponse;

public interface WishlistService {

    CursorResponse<WishlistResponse> getWishlist(Long userId, Long cursor, int size);

    WishlistResponse addToWishlist(Long userId, Long productId);

    void removeFromWishlist(Long userId, Long wishId);
}
