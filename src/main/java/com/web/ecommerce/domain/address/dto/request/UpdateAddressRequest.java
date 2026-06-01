package com.web.ecommerce.domain.address.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UpdateAddressRequest {

    @NotBlank
    private String roadNameAddress;

    @NotBlank
    private String addressDetail;
}
