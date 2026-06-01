package com.web.ecommerce.domain.wishlist.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WishlistResponse {

    private Long wishId;
    private Long productId;
    private String productName;
    private String imageUrl;
    private int minPrice;
    private int maxPrice;
}
