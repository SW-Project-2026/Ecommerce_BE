package com.web.ecommerce.domain.address.mapper;

import com.web.ecommerce.domain.address.dto.response.AddressResponse;
import com.web.ecommerce.domain.address.entity.Address;
import org.springframework.stereotype.Component;

@Component
public class AddressMapper {

    public AddressResponse toResponse(Address address) {
        return AddressResponse.builder()
                .addressId(address.getId())
                .isDefault(address.isDefault())
                .roadNameAddress(address.getRoadNameAddress())
                .addressDetail(address.getAddressDetail())
                .build();
    }
}
