package com.web.ecommerce.domain.cart.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
@Schema(description = "장바구니 수량 변경 요청")
public class UpdateCartRequest {

    @Min(1)
    @Schema(description = "변경할 수량", example = "5")
    private int quantity;
}
