package com.web.ecommerce.domain.address.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressResponse {

    private Long addressId;
    private boolean isDefault;
    private String roadNameAddress;
    private String addressDetail;
}
