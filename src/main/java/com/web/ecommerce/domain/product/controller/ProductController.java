package com.web.ecommerce.domain.product.controller;

import com.web.ecommerce.domain.product.dto.ProductSearchRequest;
import com.web.ecommerce.domain.product.dto.ProductSearchResult;
import com.web.ecommerce.domain.product.service.ProductService;
import com.web.ecommerce.global.response.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product", description = "상품 검색 API")
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @Operation(summary = "상품 검색", description = "네이버 쇼핑 API를 통해 상품을 검색합니다.")
    @GetMapping("/search")
    public ResponseEntity<BaseResponse<ProductSearchResult>> searchProducts(
            @Valid @ModelAttribute ProductSearchRequest request) {
        ProductSearchResult result = productService.searchProducts(request);
        return ResponseEntity.ok(BaseResponse.success(result));
    }


}
