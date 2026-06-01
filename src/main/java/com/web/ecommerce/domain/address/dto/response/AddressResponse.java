package com.web.ecommerce.domain.address.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "배송지 응답")
public class AddressResponse {

    @Schema(description = "배송지 ID", example = "1")
    private Long addressId;

    @Schema(description = "기본 배송지 여부", example = "true")
    private boolean isDefault;

    @Schema(description = "도로명 주소", example = "서울시 강남구 테헤란로 427")
    private String roadNameAddress;

    @Schema(description = "상세 주소", example = "101호")
    private String addressDetail;
}
