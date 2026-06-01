package com.web.ecommerce.domain.address.controller;

import com.web.ecommerce.domain.address.dto.request.CreateAddressRequest;
import com.web.ecommerce.domain.address.dto.request.UpdateAddressRequest;
import com.web.ecommerce.domain.address.dto.response.AddressResponse;
import com.web.ecommerce.domain.address.service.AddressService;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressControllerImpl implements AddressController {

    private final AddressService addressService;

    @Override
    @GetMapping
    public ResponseEntity<BaseResponse<List<AddressResponse>>> getAddresses(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(BaseResponse.success(addressService.getAddresses(userPrincipal.id())));
    }

    @Override
    @PostMapping
    public ResponseEntity<BaseResponse<AddressResponse>> createAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateAddressRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.success(201, "배송지가 추가되었습니다.", addressService.createAddress(userPrincipal.id(), request)));
    }

    @Override
    @PutMapping("/{addressId}")
    public ResponseEntity<BaseResponse<AddressResponse>> updateAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long addressId,
            @Valid @RequestBody UpdateAddressRequest request) {
        return ResponseEntity.ok(BaseResponse.success(addressService.updateAddress(userPrincipal.id(), addressId, request)));
    }

    @Override
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddress(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long addressId) {
        addressService.deleteAddress(userPrincipal.id(), addressId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PatchMapping("/{addressId}/default")
    public ResponseEntity<BaseResponse<AddressResponse>> setDefault(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long addressId) {
        return ResponseEntity.ok(BaseResponse.success(addressService.setDefault(userPrincipal.id(), addressId)));
    }
}
