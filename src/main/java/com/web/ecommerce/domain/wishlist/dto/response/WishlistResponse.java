package com.web.ecommerce.domain.wishlist.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "찜 목록 응답")
public class WishlistResponse {

    @Schema(description = "찜 ID", example = "1")
    private Long wishId;

    @Schema(description = "상품 ID", example = "3")
    private Long productId;

    @Schema(description = "상품명", example = "무선 헤드셋")
    private String productName;

    @Schema(description = "상품 이미지 URL")
    private String imageUrl;

    @Schema(description = "최저가", example = "25000")
    private int minPrice;

    @Schema(description = "최고가", example = "35000")
    private int maxPrice;
}
