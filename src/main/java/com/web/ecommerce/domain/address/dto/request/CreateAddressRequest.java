package com.web.ecommerce.domain.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateAddressRequest {

    @NotBlank
    private String roadNameAddress;

    @NotBlank
    private String addressDetail;

    private boolean isDefault;
}
