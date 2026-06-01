package com.web.ecommerce.domain.address.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@Schema(description = "배송지 수정 요청")
public class UpdateAddressRequest {

    @NotBlank
    @Schema(description = "도로명 주소", example = "서울시 서초구 반포대로 58")
    private String roadNameAddress;

    @NotBlank
    @Schema(description = "상세 주소", example = "202호")
    private String addressDetail;
}
