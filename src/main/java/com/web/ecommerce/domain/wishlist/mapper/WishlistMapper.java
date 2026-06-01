package com.web.ecommerce.domain.wishlist.mapper;

import com.web.ecommerce.domain.wishlist.dto.response.WishlistResponse;
import com.web.ecommerce.domain.wishlist.entity.Wishlist;
import org.springframework.stereotype.Component;

@Component
public class WishlistMapper {

    public WishlistResponse toResponse(Wishlist wishlist) {
        return WishlistResponse.builder()
                .wishId(wishlist.getId())
                .productId(wishlist.getProduct().getProductId())
                .productName(wishlist.getProduct().getName())
                .imageUrl(wishlist.getProduct().getImageUrl())
                .minPrice(wishlist.getProduct().getMinPrice())
                .maxPrice(wishlist.getProduct().getMaxPrice())
                .build();
    }
}
