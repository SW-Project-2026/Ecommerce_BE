package com.web.ecommerce.domain.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "배송지 추가 요청")
public class CreateAddressRequest {

    @NotBlank
    @Schema(description = "도로명 주소", example = "서울시 강남구 테헤란로 427")
    private String roadNameAddress;

    @NotBlank
    @Schema(description = "상세 주소", example = "101호")
    private String addressDetail;

    @Schema(description = "기본 배송지 여부", example = "true")
    private boolean isDefault;
}
