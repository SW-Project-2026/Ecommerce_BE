package com.web.ecommerce.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@Schema(title = "ProductSearchResult", description = "상품 검색 결과")
public class ProductSearchResult {

    @Schema(description = "전체 검색 결과 수")
    private int total;

    @Schema(description = "현재 페이지 시작 위치")
    private int start;

    @Schema(description = "현재 페이지 표시 수")
    private int display;

    @Schema(description = "상품 목록")
    private List<ProductResponse> products;
}
