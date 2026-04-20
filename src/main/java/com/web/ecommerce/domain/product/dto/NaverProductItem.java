package com.web.ecommerce.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Schema(title = "상품 정보", description = "상품 하나하나 HTML태그 포함된 지저분한 데이터")
public class NaverProductItem {

    private String title;

    private String link;

    private String image;

    private String lprice;

    private String hprice;

    private String mallName;

    private String productId;

    private String brand;

    private String maker;

    private String category1;

    private String category2;

    private String category3;

    private String category4;
}
