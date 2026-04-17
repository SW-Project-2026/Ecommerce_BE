package com.web.ecommerce.domain.product.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(title = "ProductResponse DTO", description = "네이버 쇼핑 상품 응답")
public class ProductResponse {

    @Schema(description = "상품 ID", example = "123456789")
    private String productId;

    @Schema(description = "상품명")
    private String title;

    @Schema(description = "상품 링크")
    private String link;

    @Schema(description = "상품 이미지 URL")
    private String image;

    @Schema(description = "최저가", example = "10000")
    private String lowestPrice;

    @Schema(description = "최고가", example = "15000")
    private String highestPrice;

    @Schema(description = "판매처")
    private String mallName;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "제조사")
    private String maker;

    @Schema(description = "카테고리 1")
    private String category1;

    @Schema(description = "카테고리 2")
    private String category2;

    public static ProductResponse from(NaverProductItem item) {
        return ProductResponse.builder()
                .productId(item.getProductId())
                .title(item.getTitle().replaceAll("<[^>]*>", ""))
                .link(item.getLink())
                .image(item.getImage())
                .lowestPrice(item.getLprice())
                .highestPrice(item.getHprice())
                .mallName(item.getMallName())
                .brand(item.getBrand())
                .maker(item.getMaker())
                .category1(item.getCategory1())
                .category2(item.getCategory2())
                .build();
    }
}
