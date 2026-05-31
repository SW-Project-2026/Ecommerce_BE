package com.web.ecommerce.domain.address.service;

import com.web.ecommerce.domain.address.dto.request.CreateAddressRequest;
import com.web.ecommerce.domain.address.dto.request.UpdateAddressRequest;
import com.web.ecommerce.domain.address.dto.response.AddressResponse;

import java.util.List;

public interface AddressService {

    List<AddressResponse> getAddresses(Long userId);

    AddressResponse createAddress(Long userId, CreateAddressRequest request);

    AddressResponse updateAddress(Long userId, Long addressId, UpdateAddressRequest request);

    void deleteAddress(Long userId, Long addressId);

    AddressResponse setDefault(Long userId, Long addressId);
}
