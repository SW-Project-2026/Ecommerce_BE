package com.web.ecommerce.domain.cart.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "장바구니 응답")
public class CartResponse {

    @Schema(description = "장바구니 ID", example = "1")
    private Long cartId;

    @Schema(description = "상품 ID", example = "3")
    private Long productId;

    @Schema(description = "상품명", example = "무선 헤드셋")
    private String productName;

    @Schema(description = "상품 이미지 URL")
    private String imageUrl;

    @Schema(description = "단가", example = "30000")
    private int unitPrice;

    @Schema(description = "수량", example = "2")
    private int quantity;

    @Schema(description = "소계 (단가 × 수량)", example = "60000")
    private int subtotal;
}
