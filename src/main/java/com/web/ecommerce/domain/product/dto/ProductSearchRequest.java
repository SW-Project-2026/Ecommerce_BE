package com.web.ecommerce.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(title = "ProductSearchRequest", description = "상품 검색 요청")
public class ProductSearchRequest {

    @NotBlank(message = "검색어를 입력해주세요.")
    @Schema(description = "검색어", example = "케이크")
    private String query;

    @Min(1) @Max(100)
    @Schema(description = "한 번에 표시할 검색 결과 수 (1~100)", example = "10")
    private int display = 10;

    @Min(1) @Max(1000)
    @Schema(description = "검색 시작 위치 (1~1000)", example = "1")
    private int start = 1;

    @Schema(description = "정렬 기준 (sim: 유사도순, date: 날짜순, asc: 가격 오름차순, dsc: 가격 내림차순)", example = "sim")
    private String sort = "sim";

}
