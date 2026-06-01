package com.web.ecommerce.domain.cart.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class UpdateCartRequest {

    @Min(1)
    private int quantity;
}
