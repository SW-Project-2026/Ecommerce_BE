package com.web.ecommerce.domain.product.dto.response;

import com.web.ecommerce.domain.product.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@Schema(title = "ProductDetailResponse", description = "상품 상세 응답")
public class ProductDetailResponse {

    @Schema(description = "상품 ID", example = "1")
    private Long productId;

    @Schema(description = "상품명", example = "딸기 케이크")
    private String name;

    @Schema(description = "상품 설명")
    private String description;

    @Schema(description = "최저가", example = "30000")
    private int minPrice;

    @Schema(description = "최고가", example = "40000")
    private int maxPrice;

    @Schema(description = "재고 수량", example = "100")
    private int stockQuantity;

    @Schema(description = "카테고리", example = "패션")
    private String productCategory;

    @Schema(description = "활성화 여부", example = "1")
    private Integer isActive;

    @Schema(description = "네이버 상품 ID")
    private String naverProductId;

    @Schema(description = "세부 카테고리", example = "과자/베이커리")
    private String subCategory;

    @Schema(description = "검색 키워드 (가전/디지털, 패션, 뷰티, 식품, 생활용품, 스포츠)")
    private String searchKeyword;

    @Schema(description = "이미지 URL")
    private String imageUrl;

    @Schema(description = "브랜드")
    private String brand;

    @Schema(description = "세부 카테고리2", example = "베이커리")
    private String category2;

    @Schema(description = "생성일시")
    private LocalDateTime createdAt;

    @Schema(description = "수정일시")
    private LocalDateTime updatedAt;

    public static ProductDetailResponse from(Product product) {
        return ProductDetailResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .minPrice(product.getMinPrice())
                .maxPrice(product.getMaxPrice())
                .stockQuantity(product.getStockQuantity())
                .productCategory(product.getProductCategory())
                .isActive(product.getIsActive())
                .naverProductId(product.getNaverProductId())
                .subCategory(product.getSubCategory())
                .searchKeyword(product.getSearchKeyword())
                .imageUrl(product.getImageUrl())
                .brand(product.getBrand())
                .category2(product.getCategory2())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
