package com.web.ecommerce.domain.address.service;

import com.web.ecommerce.domain.address.dto.request.CreateAddressRequest;
import com.web.ecommerce.domain.address.dto.request.UpdateAddressRequest;
import com.web.ecommerce.domain.address.dto.response.AddressResponse;
import com.web.ecommerce.domain.address.entity.Address;
import com.web.ecommerce.domain.address.exception.AddressErrorCode;
import com.web.ecommerce.domain.address.mapper.AddressMapper;
import com.web.ecommerce.domain.address.repository.AddressRepository;
import com.web.ecommerce.domain.user.entity.User;
import com.web.ecommerce.domain.user.exception.UserErrorCode;
import com.web.ecommerce.domain.user.repository.UserRepository;
import com.web.ecommerce.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final AddressMapper addressMapper;

    @Override
    @Transactional(readOnly = true)
    public List<AddressResponse> getAddresses(Long userId) {
        return addressRepository.findAllByUserId(userId).stream()
                .map(addressMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse createAddress(Long userId, CreateAddressRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(UserErrorCode.USER_NOT_FOUND));

        if (request.isDefault()) {
            addressRepository.clearDefaultByUserId(userId);
        }

        Address address = Address.builder()
                .user(user)
                .roadNameAddress(request.getRoadNameAddress())
                .addressDetail(request.getAddressDetail())
                .isDefault(request.isDefault())
                .build();

        return addressMapper.toResponse(addressRepository.save(address));
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(Long userId, Long addressId, UpdateAddressRequest request) {
        Address address = getAddressOwnedByUser(userId, addressId);
        address.update(request.getRoadNameAddress(), request.getAddressDetail());
        return addressMapper.toResponse(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long userId, Long addressId) {
        Address address = getAddressOwnedByUser(userId, addressId);
        addressRepository.delete(address);
    }

    @Override
    @Transactional
    public AddressResponse setDefault(Long userId, Long addressId) {
        Address address = getAddressOwnedByUser(userId, addressId);
        addressRepository.clearDefaultByUserId(userId);
        address.setDefault(true);
        return addressMapper.toResponse(address);
    }

    private Address getAddressOwnedByUser(Long userId, Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new CustomException(AddressErrorCode.ADDRESS_NOT_FOUND));
        if (!address.getUser().getId().equals(userId)) {
            throw new CustomException(AddressErrorCode.ADDRESS_ACCESS_DENIED);
        }
        return address;
    }
}
