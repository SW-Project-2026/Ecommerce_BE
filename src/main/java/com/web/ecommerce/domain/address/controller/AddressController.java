package com.web.ecommerce.domain.address.controller;

import com.web.ecommerce.domain.address.dto.request.CreateAddressRequest;
import com.web.ecommerce.domain.address.dto.request.UpdateAddressRequest;
import com.web.ecommerce.domain.address.dto.response.AddressResponse;
import com.web.ecommerce.global.response.BaseResponse;
import com.web.ecommerce.global.security.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Tag(name = "Address", description = "배송지 API")
public interface AddressController {

    @Operation(summary = "내 배송지 목록 조회")
    ResponseEntity<BaseResponse<List<AddressResponse>>> getAddresses(UserPrincipal userPrincipal);

    @Operation(summary = "배송지 추가")
    ResponseEntity<BaseResponse<AddressResponse>> createAddress(UserPrincipal userPrincipal, CreateAddressRequest request);

    @Operation(summary = "배송지 수정")
    ResponseEntity<BaseResponse<AddressResponse>> updateAddress(UserPrincipal userPrincipal, Long addressId, UpdateAddressRequest request);

    @Operation(summary = "배송지 삭제")
    ResponseEntity<Void> deleteAddress(UserPrincipal userPrincipal, Long addressId);

    @Operation(summary = "기본 배송지 설정")
    ResponseEntity<BaseResponse<AddressResponse>> setDefault(UserPrincipal userPrincipal, Long addressId);
}
