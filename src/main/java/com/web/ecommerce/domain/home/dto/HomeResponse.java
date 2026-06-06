package com.web.ecommerce.domain.home.dto;

import java.util.List;

public record HomeResponse(
        String userName,
        List<ProductItem> recommendedProducts,
        List<ProductItem> recentViewedProducts,
        List<ProductItem> purchasedProducts,
        List<BestProductItem> bestProducts,
        List<CouponItem> promotions
) {
    public record ProductItem(
            Long productId,
            String productName,
            int price,
            String category,
            String imageUrl,
            boolean isWishlisted
    ) {}

    public record BestProductItem(
            int rank,
            Long productId,
            String productName,
            int price,
            String category,
            String imageUrl,
            boolean isWishlisted
    ) {}

    public record CouponItem(
            Long couponId,
            String couponName,
            int discountAmount,
            String expiredAt,
            boolean hasReceived
    ) {}
}
