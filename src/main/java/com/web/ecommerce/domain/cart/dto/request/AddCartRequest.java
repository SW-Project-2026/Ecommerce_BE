package com.web.ecommerce.domain.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 상품 추가 요청")
public class AddCartRequest {

    @NotNull
    @Schema(description = "상품 ID", example = "3")
    private Long productId;

    @Min(1)
    @Schema(description = "수량", example = "2")
    private int quantity;
}
