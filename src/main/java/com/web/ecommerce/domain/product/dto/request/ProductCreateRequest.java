package com.web.ecommerce.domain.product.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;

@Getter
@Schema(title = "ProductCreateRequest", description = "상품 등록 요청")
public class ProductCreateRequest {

    @NotBlank(message = "상품명을 입력해주세요.")
    @Size(max = 100)
    @Schema(description = "상품명", example = "딸기 케이크")
    private String name;

    @NotBlank(message = "상품 설명을 입력해주세요.")
    @Size(max = 2000)
    @Schema(description = "상품 설명", example = "신선한 딸기로 만든 케이크입니다.")
    private String description;

    @Min(value = 0, message = "가격은 0 이상이어야 합니다.")
    @Schema(description = "가격", example = "35000")
    private int price;

    @Min(value = 0, message = "재고는 0 이상이어야 합니다.")
    @Schema(description = "재고 수량", example = "100")
    private int stockQuantity;

    @Size(max = 50)
    @Schema(description = "카테고리", example = "패션")
    private String productCategory;

    @Size(max = 500)
    @Schema(description = "이미지 URL", example = "https://example.com/image.jpg")
    private String imageUrl;
}
